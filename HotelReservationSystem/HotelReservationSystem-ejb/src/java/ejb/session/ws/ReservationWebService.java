/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.CustomersEntitySessionBeanLocal;
import ejb.session.stateless.HotelRoomsEntitySessionBeanLocal;
import ejb.session.stateless.RatesEntitySessionBeanLocal;
import ejb.session.stateless.ReservationsEntitySessionBeanLocal;
import ejb.session.stateless.RoomTypesEntitySessionBeanLocal;
import entity.Customers;
import entity.HotelRooms;
import entity.Rates;
import entity.Reservations;
import entity.RoomTypes;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.InvalidLoginCredentialException;
import util.exception.InvalidRoomQuantityException;
import util.exception.InvalidRoomTypeException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author tshuenhau
 */
@WebService(serviceName = "ReservationWebService")
@Stateless()
public class ReservationWebService {

    @EJB
    private RoomTypesEntitySessionBeanLocal roomTypesEntitySessionBeanLocal;

    SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");

    @EJB
    private RatesEntitySessionBeanLocal ratesEntitySessionBeanLocal;

    @EJB
    private ReservationsEntitySessionBeanLocal reservationsEntitySessionBeanLocal;

    @EJB
    private HotelRoomsEntitySessionBeanLocal hotelRoomsEntitySessionBeanLocal;

    @EJB
    private CustomersEntitySessionBeanLocal customersEntitySessionBeanLocal;
    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");//"dd/MM/yyyy hh:mm a"

    @WebMethod(operationName="ViewReservation")
    public Reservations viewReservation(Long passportNum, String password, Long reservationID) throws InvalidLoginCredentialException, ReservationNotFoundException{
        List<Reservations> reservations = ViewAllReservations(passportNum,password);
        if(reservations!=null){
            for(Reservations r: reservations){
                if(r.getReservationID().equals(reservationID)){
                    return r;
                }
            }
        }
        
        throw new ReservationNotFoundException();
        
    }
    @WebMethod(operationName = "ViewAllReservations")
    public List<Reservations> ViewAllReservations(Long passportNum, String password) throws InvalidLoginCredentialException {
        Customers c = Login(passportNum, password);
        List<Reservations> allReservations = reservationsEntitySessionBeanLocal.retrieveAllReservations();
        List<Reservations> relaventReservations = new ArrayList<>();
        if (c != null) {
            for (Reservations r : allReservations) {
                em.detach(r);
                if (r.getReservedBy().equals(c)) {
                    relaventReservations.add(r);
                    r.setReservedBy(null);
                }
            }
        }

        return relaventReservations;

    }

    @WebMethod(operationName = "Login")
    public Customers Login(Long passPortNum, String password) throws InvalidLoginCredentialException {
        List<Customers> customers = customersEntitySessionBeanLocal.retrieveAllCustomers();
        //em.detach(this);
        for (Customers c : customers) {
            em.detach(c);
            for (Reservations r : c.getReservations()) {
                em.detach(r);
                r.setReservedBy(null);
            }
            if (c.getPassportNum().equals(passPortNum) && c.getIsPartner() == true) {
                if (c.getPassword().equals(password)) {
                    return c;
                }
            }
        }
        throw new InvalidLoginCredentialException();

    }

    @WebMethod(operationName = "SearchRoom")
    public String[][] searchHotelRooms(String inputCheckInDate, String inputCheckOutDate) throws ParseException {
        String[][] result = new String[5][3];
        Date checkInDate = inputDateFormat.parse(inputCheckInDate);
        Date checkOutDate = inputDateFormat.parse(inputCheckOutDate);

        //System.out.println(checkInDate);
        Map<String, List<Integer>> rooms = doSearchRoom(checkInDate, checkOutDate);
        rooms = doCalculateCost(checkInDate, checkOutDate, rooms);

        List<RoomTypes> roomTypes = roomTypesEntitySessionBeanLocal.retrieveAllRoomTypes();
        for(RoomTypes r: roomTypes){
            rooms.put(r.getRoomTypeName(), Arrays.asList(0, 0, 0));
        }
        Integer count = rooms.size();
        for(int i = 0; i< count ; i++){
                result[0][0] = roomTypes.get(i).getRoomTypeName();
                result[i][1] = rooms.get(result[i][0]).get(0).toString();
                result[i][2] = rooms.get(result[i][0]).get(1).toString();
        }
//        result[0][0] = "Deluxe Room";
//        result[1][0] = "Premier Room";
//        result[2][0] = "Family Room";
//        result[3][0] = "Junior Suite";
//        result[4][0] = "Grand Suite";
//
//        for (int i = 0; i < 5; i++) {
//            //if(rooms.get(result[i][0]) !=null)
//            {
//                
//            }
//
//        }

//        for (Map.Entry room : rooms.entrySet()){
//            
//        }
        return result;

    }

    private Map<String, List<Integer>> doCalculateCost(Date checkInDate, Date checkOutDate, Map<String, List<Integer>> roomsPreCost) {
        List<Rates> promoRates = new ArrayList<>();
        List<Rates> peakRates = new ArrayList<>();
        List<Rates> normalRates = new ArrayList<>();
        List<Rates> publishedRates = new ArrayList<>();
        Map<String, List<Integer>> rooms = roomsPreCost;

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
        return rooms;
    }

    @WebMethod(operationName = "addReservation")
    public List<Reservations> addReservation(Long passportNum, String password, String inputCheckInDate, String inputCheckOutDate, String roomType, Integer quantity) throws InvalidRoomTypeException, InvalidRoomQuantityException, ParseException, InvalidLoginCredentialException {
        String[][] data = searchHotelRooms(inputCheckInDate, inputCheckOutDate);
        Integer cost = 0;

        for (int i = 0; i < data.length; i++) {

            if (data[i][0].equals(roomType)) {
                if (Integer.parseInt(data[i][1]) < quantity) {
                    throw new InvalidRoomQuantityException("NO MORE ROOMS");
                    //return new ArrayList<Reservations>();
                }
                cost = Integer.parseInt(data[i][2]);
                System.out.println(cost);
            }

        }
        Date checkInDate = inputDateFormat.parse(inputCheckInDate);
        Date checkOutDate = inputDateFormat.parse(inputCheckOutDate);
        List<Reservations> reservations = new ArrayList<>();

        Customers c = Login(passportNum, password);
        if (c != null) {
            for (int i = 0; i < quantity; i++) {
                Reservations newReservation = new Reservations(c, roomType, checkInDate, checkOutDate, cost);
                reservationsEntitySessionBeanLocal.createNewReservation(newReservation);
                reservations.add(newReservation);

            }
        }
        for (Reservations r : reservations) {
            em.detach(r);
            r.getReservedBy().setReservations(null);
        }

        return reservations;
    }

    /**
     * This is a sample web service operation
     */
    private Map<String, List<Integer>> doSearchRoom(Date checkInDate, Date checkOutDate) {
        Map<String, List<Integer>> rooms = new HashMap<>();
//        rooms.put("Deluxe Room", Arrays.asList(0, 0, 0));
//        rooms.put("Premier Room", Arrays.asList(0, 0, 0));
//        rooms.put("Family Room", Arrays.asList(0, 0, 0));
//        rooms.put("Junior Suite", Arrays.asList(0, 0, 0));
//        rooms.put("Grand Suite", Arrays.asList(0, 0, 0));
        
        List<RoomTypes> roomTypes = roomTypesEntitySessionBeanLocal.retrieveAllRoomTypes();
        for(RoomTypes r: roomTypes){
            rooms.put(r.getRoomTypeName(), Arrays.asList(0, 0, 0));
        }

        List<HotelRooms> allHotelRooms = hotelRoomsEntitySessionBeanLocal.retrieveAllHotelRooms();

        for (HotelRooms hotelRoom : allHotelRooms) {
            if (hotelRoom.getStatus() == false) {
                continue;
            }
            List<Integer> currList = new ArrayList<Integer>();

            if (rooms.containsKey(hotelRoom.getRmType())) {
                currList = rooms.get(hotelRoom.getRmType());
            }
            List<Integer> newList = new ArrayList<Integer>();
            newList.add(currList.get(0) + 1);
            newList.add(currList.get(1));
            String temp = hotelRoom.getRmType().getRoomTypeName();
            System.out.println(temp);

            rooms.put(temp, newList);
        }
        List<Reservations> allReservations = reservationsEntitySessionBeanLocal.retrieveAllReservations();
        for (Reservations r : allReservations) {
            em.detach(r);
            r.setReservedBy(null);
        }
        List<Reservations> toRemove = new ArrayList<Reservations>();
        for (Reservations r : allReservations) {
            System.out.println(checkInDate);
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
        return rooms;
    }

    private boolean isWithinRange(Date date, Date startDate, Date endDate) {
        return !(date.before(startDate) || date.after(endDate));
    }


}
