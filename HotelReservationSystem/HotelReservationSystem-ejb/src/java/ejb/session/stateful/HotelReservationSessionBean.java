/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import ejb.session.stateless.HotelRoomsEntitySessionBeanLocal;
import ejb.session.stateless.RatesEntitySessionBeanLocal;
import ejb.session.stateless.ReservationsEntitySessionBeanLocal;
import entity.Customers;
import entity.HotelRooms;
import entity.Rates;
import entity.Reservations;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import util.exception.InvalidRoomQuantityException;
import util.exception.InvalidRoomTypeException;

/**
 *
 * @author tshuenhau
 */
@Stateful
public class HotelReservationSessionBean implements HotelReservationSessionBeanRemote {

    @EJB
    private RatesEntitySessionBeanLocal ratesEntitySessionBeanLocal;

    @EJB
    private ReservationsEntitySessionBeanLocal reservationsEntitySessionBeanLocal;

    @EJB
    private HotelRoomsEntitySessionBeanLocal hotelRoomsEntitySessionBeanLocal;
    Customers currentCustomer;
    SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");//"dd/MM/yyyy hh:mm a"
    Date checkInDate;
    Date checkOutDate;
    //private List<Rates> relaventRates;
    List<Rates> promoRates = new ArrayList<>();
    List<Rates> peakRates = new ArrayList<>();
    List<Rates> normalRates = new ArrayList<>();
    List<Rates> publishedRates = new ArrayList<>();
    //private Map<String,List<Integer>> availability = new HashMap();
    private Map<String, List<Integer>> rooms = new HashMap();//room type -- availability -- cost

    List<Reservations> reservations = new ArrayList<>();

    @Remove
    @Override
    public void remove() {
        // Do nothing
    }

    @PreDestroy
    public void preDestroy() {
        if (rooms != null) {
            rooms.clear();
        }
        if (reservations != null) {
            reservations.clear();
        }
    }

    @Override
    public void login(Customers customer) {
        this.currentCustomer = customer;
    }

    @Override
    public Map<String, List<Integer>> searchHotelRooms(Date checkInDate, Date checkOutDate) {
        doSearchRoom(checkInDate, checkOutDate);
        doCalculateCost();
        return rooms;
    }
    
    @Override
    public Map<String, List<Integer>> walkInSearchHotelRooms(Date checkInDate, Date checkOutDate) {
        doSearchRoom(checkInDate, checkOutDate);
        doWalkInCalculateCost();
        return rooms;
    }
    
    private void doSearchRoom(Date checkInDate, Date checkOutDate){
    this.checkInDate = checkInDate;
    this.checkOutDate = checkOutDate;
        reservations.clear();
        rooms.clear();
        rooms.put("Deluxe Room", Arrays.asList(0, 0, 0));
        rooms.put("Premier Room", Arrays.asList(0, 0, 0));
        rooms.put("Family Room", Arrays.asList(0, 0, 0));
        rooms.put("Junior Suite", Arrays.asList(0, 0, 0));
        rooms.put("Grand Suite", Arrays.asList(0, 0, 0));

        List<HotelRooms> allHotelRooms = hotelRoomsEntitySessionBeanLocal.retrieveAllHotelRooms();

        for (HotelRooms hotelRoom : allHotelRooms) {
            if (hotelRoom.getStatus() == false) {
                System.out.println("False");
                continue;
            }
            List<Integer> currList = new ArrayList<Integer>();

            if (rooms.containsKey(hotelRoom.getRmType())) {
                currList = rooms.get(hotelRoom.getRmType());
            }
            List<Integer> newList = new ArrayList<Integer>();
            newList.add(currList.get(0) + 1);
            newList.add(currList.get(1));
            rooms.put(hotelRoom.getRmType(), newList);
        }
        List<Reservations> allReservations = reservationsEntitySessionBeanLocal.retrieveAllReservations();
        List<Reservations> toRemove = new ArrayList<Reservations>();
        for (Reservations r : allReservations) {
            if (r.getStartDate().compareTo(checkOutDate) >= 0) {
                toRemove.add(r);
            } else if (r.getEndDate().compareTo(checkInDate) <= 0) {
                toRemove.add(r);
            }
        }
        allReservations.removeAll(toRemove);

        for (Reservations reservation : allReservations) {
            List<Integer> currList = new ArrayList<Integer>();
            if (rooms.containsKey(reservation.getRoomType())) {
                currList = rooms.get(reservation.getRoomType());
            }
            List<Integer> newList = new ArrayList<Integer>();
            newList.add(currList.get(0) - 1);
            newList.add(currList.get(1));
            rooms.put(reservation.getRoomType(), newList);
        }
    }

    private void doCalculateCost() {
        System.out.println("Calculating Rates...");
        List<Rates> rates = ratesEntitySessionBeanLocal.retrieveAllRates();
        List<Rates> relaventRates = new ArrayList();
        for (Rates r : rates) { // get all rates within the date
            if (r.getRateType().equals("Normal")) {
                relaventRates.add(r);
            } else if ((r.getRateType().equals("Peak") || r.getRateType().equals("Promo")) && r.getStartDate() != null && r.getEndDate() != null) {
                relaventRates.add(r);
            }
        }
        for (Rates r : relaventRates) {
            System.out.println(r);
        }
        promoRates = new ArrayList<>();
        peakRates = new ArrayList<>();
        normalRates = new ArrayList<>();
        publishedRates = new ArrayList<>();

        for (Rates r : relaventRates) {
            if (r.getRateType().equals("Promo")) {
                promoRates.add(r);
            } else if (r.getRateType().equals("Peak")) {
                peakRates.add(r);
            } else if (r.getRateType().equals("Normal")) {
                normalRates.add(r);
            } else if (r.getRateType().equals("Published")) {
                publishedRates.add(r);
            }
        }
        GregorianCalendar gcal = new GregorianCalendar();
        gcal.setTime(checkInDate);
        while (!gcal.getTime().after(checkOutDate)) {
            Date d = gcal.getTime();
            if (d.equals(checkOutDate)) {
                break;
            }
            System.out.println(outputDateFormat.format(d));

            for (Map.Entry room : rooms.entrySet()) {
                Boolean prioritySet = false;
                for (Rates r : promoRates) {
                    if (room.getKey().equals(r.getRoomType())) {
                        if (r.getRateType().equals("Promo") && r.getStartDate() != null && r.getEndDate() != null) { // also need to check if this date is within the range
                            if (isWithinRange(d, r.getStartDate(), r.getEndDate())) {
                                List<Integer> temp = (List<Integer>) room.getValue();
                                System.out.println(temp.toString());
                                temp.set(1, temp.get(1) + r.getPrice());
                                System.out.println("this date " + outputDateFormat.format(d) + " using rate " + r.toString());
                                rooms.put(r.getRoomType(), temp);
                                prioritySet = true;
                                break;
                            }
                        }

                    }
                }

                if (prioritySet != true) {
                    for (Rates r : peakRates) {
                        if (room.getKey().equals(r.getRoomType())) {
                            if (r.getRateType().equals("Peak") && r.getStartDate() != null && r.getEndDate() != null) { // also need to check if this date is within the range
                                if (isWithinRange(d, r.getStartDate(), r.getEndDate())) {
                                    List<Integer> temp = (List<Integer>) room.getValue();
                                    System.out.println(temp.toString());
                                    temp.set(1, temp.get(1) + r.getPrice());
                                    System.out.println("this date " + outputDateFormat.format(d) + " using rate " + r.toString());
                                    rooms.put(r.getRoomType(), temp);
                                    prioritySet = true;
                                    break;
                                }
                            }
                        }

                    }
                }
                if (prioritySet != true) {
                    for (Rates r : normalRates) {
                        if (room.getKey().equals(r.getRoomType())) {
                            if (r.getRateType().equals("Normal")) {
                                List<Integer> temp = (List<Integer>) room.getValue();
                                System.out.println(temp.toString());
                                temp.set(1, temp.get(1) + r.getPrice());
                                System.out.println("this date " + outputDateFormat.format(d) + " using rate " + r.toString());
                                rooms.put(r.getRoomType(), temp);
                                prioritySet = true;
                                break;

                            }
                        }

                    }
                }
            }

            gcal.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    private void doWalkInCalculateCost() {
        System.out.println("Calculating Rates...");
        List<Rates> rates = ratesEntitySessionBeanLocal.retrieveAllRates();
        publishedRates = new ArrayList<>();
        for (Rates r : rates) {
            if (r.getRateType().equals("Published")) {
                publishedRates.add(r);
            }
        }
        for (Rates r : publishedRates) {
            System.out.println(r);
        }
        GregorianCalendar gcal = new GregorianCalendar();
        gcal.setTime(checkInDate);
        while (!gcal.getTime().after(checkOutDate)) {
            Date d = gcal.getTime();
            if (d.equals(checkOutDate)) {
                break;
            }
            System.out.println(outputDateFormat.format(d));
            for (Map.Entry room : rooms.entrySet()) {
                for (Rates r : publishedRates) {
                    if (room.getKey().equals(r.getRoomType())) {
                        if (r.getRateType().equals("Published")) {
                            List<Integer> temp = (List<Integer>) room.getValue();
                            System.out.println(temp.toString());
                            temp.set(1, temp.get(1) + r.getPrice());
                            System.out.println("this date " + outputDateFormat.format(d) + " using rate " + r.toString());
                            rooms.put(r.getRoomType(), temp);
                            break;
                        }
                    }
                }
            }

            gcal.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    @Override
    public boolean isWithinRange(Date date, Date startDate, Date endDate) {
        return !(date.before(startDate) || date.after(endDate));
    }

    @Override
    public List<Reservations> addReservation(String roomType, Integer quantity) throws InvalidRoomTypeException, InvalidRoomQuantityException {
        if (rooms.containsKey(roomType)) {
            if (rooms.get(roomType).get(0) >= quantity) {
                List<Integer> newList = new ArrayList<>();
                Integer qty = rooms.get(roomType).get(0);
                for (int i = 0; i < quantity; i++) {
                    Reservations newReservation = new Reservations(currentCustomer, roomType, checkInDate, checkOutDate, (float) rooms.get(roomType).get(1));
                    reservations.add(newReservation);
                    qty -= 1;

                }
                newList.add(qty);
                newList.add(rooms.get(roomType).get(1));
                rooms.put(roomType, newList);

            } else {
                throw new InvalidRoomQuantityException("Invalid Quantity for " + roomType + " Only " + rooms.get(roomType).get(0) + " rooms available");
            }
        } else {
            throw new InvalidRoomTypeException("Invalid Room Type: " + roomType);
        }
        return reservations;
    }

    public List<Reservations> confirmReservations() {
        for (Reservations r : reservations) {
            reservationsEntitySessionBeanLocal.createNewReservation(r);
        }

        return reservations;
    }

    public Integer getTotalCost() {
        int cost = 0;
        for (Reservations r : reservations) {
            cost += r.getCost();
        }

        return cost;
    }

    public Map<String, Integer> getRoomQuantities() { // ROom Type --- Quantity
        Map<String, Integer> result = new HashMap<String, Integer>();
        for (Reservations r : reservations) {
            if (result.containsKey(r.getRoomType())) {
                result.put(r.getRoomType(), result.get(r.getRoomType()) + 1);
            } else {
                result.put(r.getRoomType(), 1);
            }
        }
        return result;
    }

}
