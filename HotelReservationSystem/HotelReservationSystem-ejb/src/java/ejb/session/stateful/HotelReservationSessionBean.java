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
    SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");//"dd/MM/yyyy hh:mm a"
    Date checkInDate;
    Date checkOutDate;
    //private List<Rates> relaventRates;
    List<Rates> promoRates = new ArrayList<>();
    List<Rates> peakRates = new ArrayList<>();
    List<Rates> normalRates = new ArrayList<>();
    List<Rates> publishedRates = new ArrayList<>();
    private Map<String,List<Integer>> availability = new HashMap();
    private Map<String,List<Integer>> rooms = new HashMap();//room type -- availability -- cost
    
    List<Reservations> reservations = new ArrayList<>();

    
    @Remove
    @Override
    public void remove()
    {
        // Do nothing
    }
    @PreDestroy
    public void preDestroy()
    {
        if(availability != null || rooms != null)
        {
            availability.clear();
            rooms.clear();
        }
    }
    
    @Override
    public Map<String, List<Integer>> searchHotelRooms(Date checkInDate, Date checkOutDate){
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        availability.put("Deluxe Room", Arrays.asList(0,0,0));
        availability.put("Premier Room", Arrays.asList(0,0,0));
        availability.put("Family Room", Arrays.asList(0,0,0));
        availability.put("Junior Suite", Arrays.asList(0,0,0));
        availability.put("Grand Suite", Arrays.asList(0,0,0));

        List<HotelRooms> allHotelRooms = hotelRoomsEntitySessionBeanLocal.retrieveAllHotelRooms();
        
        for(HotelRooms hotelRoom : allHotelRooms){
            if(hotelRoom.getStatus() == false){continue;}
            List<Integer> currList = new ArrayList<Integer>();

            if(availability.containsKey(hotelRoom.getRmType())){
                currList = availability.get(hotelRoom.getRmType());
            }
            List<Integer> newList = new ArrayList<Integer>();
            newList.add(currList.get(0) + 1);
            newList.add(currList.get(1));
            newList.add(currList.get(2));

            availability.put(hotelRoom.getRmType(), newList);
        }
        List<Reservations> allReservations = reservationsEntitySessionBeanLocal.retrieveAllReservations();
        List<Reservations> toRemove = new ArrayList<Reservations>();
        for(Reservations r: allReservations){
            if(r.getStartDate().compareTo(checkOutDate) >= 0){
                toRemove.add(r);
            }
            else if(r.getEndDate().compareTo(checkInDate) <= 0){
                toRemove.add(r);
            }
        }
        allReservations.removeAll(toRemove);
        
        for(Reservations reservation : allReservations){
            List<Integer> currList = new ArrayList<Integer>();
            if(availability.containsKey(reservation.getRoomType())){
                currList = availability.get(reservation.getRoomType());
            }
            List<Integer> newList = new ArrayList<Integer>();
            newList.add(currList.get(0));
            newList.add(currList.get(1) + 1);
            newList.add(currList.get(2));

            
         
            availability.put(reservation.getRoomType(), newList);
        }
        
        Map<String, List<Integer>> result = new HashMap<String, List<Integer>>();
        
        availability.entrySet().forEach(_item -> {
            System.out.println(_item);
            int count = _item.getValue().get(0)-_item.getValue().get(1);
            List<Integer> temp = new ArrayList<Integer>();
            temp.add(count);
            temp.add(_item.getValue().get(2));
            result.put(_item.getKey(), temp);
        });
        rooms = result;
        doCalculateCost();
   
        return result; //! REMEMBER TO CHANGE THIS
    
    }
    
    private void doCalculateCost(){
        System.out.println("Calculating Rates...");
        List<Rates> rates = ratesEntitySessionBeanLocal.retrieveAllRates();
        List<Rates> relaventRates = new ArrayList();
        for(Rates r : rates) {
//                    System.out.println("Iterating...  ");
            if(r.getRateType().equals("Normal")){
//                System.out.println("Normal...  ");
//                System.out.println(r);
                relaventRates.add(r);
            }
            else if((r.getRateType().equals("Peak") || r.getRateType().equals("Promo") ) && r.getStartDate() != null && r.getEndDate() != null){
                //System.out.println("Peak/Promo...  ");
                relaventRates.add(r);
            }
        }
        
        for(Rates r : relaventRates){
            System.out.println(r);
        }
        
        GregorianCalendar gcal = new GregorianCalendar();
        gcal.setTime(checkInDate);
        //System.out.println("Calendar");
        //System.out.println(outputDateFormat.format(checkInDate));

        while(!gcal.getTime().after(checkOutDate)){
            //System.out.println(cStart);
            Date d  = gcal.getTime();
            if(d.equals(checkOutDate)){break;}
            System.out.println(outputDateFormat.format(d));
            //need to call sort first. need way to get promo first
            promoRates = new ArrayList<>();
            peakRates = new ArrayList<>();
            normalRates = new ArrayList<>();
            publishedRates = new ArrayList<>();

            for(Rates r: relaventRates){
                if(r.getRateType().equals("Promo")){
                    promoRates.add(r);
                }
                else if(r.getRateType().equals("Peak")){
                    peakRates.add(r);
                }    
                else if(r.getRateType().equals("Normal")){
                    normalRates.add(r);
                }     
                else if(r.getRateType().equals("Published")){
                    publishedRates.add(r);
                }     
            }
              
            for(Map.Entry room : rooms.entrySet()){
                Boolean prioritySet = false;
                for(Rates r: promoRates){
                    if(room.getKey().equals(r.getRoomType())){
                        if(r.getRateType().equals("Promo") && r.getStartDate() != null && r.getEndDate()!= null){ // also need to check if this date is within the range
                            if(isWithinRange(d, r.getStartDate(),r.getEndDate())){
                            //Map<String, List<Integer>> result = new HashMap<String, List<Integer>>();
                                List<Integer> temp = (List<Integer>) room.getValue();
                                System.out.println(temp.toString());
                                temp.set(1,temp.get(1) + r.getPrice());
                                System.out.println("this date " + outputDateFormat.format(d) + " using rate " + r.toString());
                                rooms.put(r.getRoomType(), temp);
                                prioritySet = true;
                                break;
                            }
                        }
                       
                        
                    }
                }
                
                if(prioritySet != true) {
                    for(Rates r : peakRates){
                        if(room.getKey().equals(r.getRoomType())){
                            if(r.getRateType().equals("Peak") && r.getStartDate() != null && r.getEndDate()!= null){ // also need to check if this date is within the range
                                if(isWithinRange(d, r.getStartDate(),r.getEndDate())){
                                //Map<String, List<Integer>> result = new HashMap<String, List<Integer>>();
                                    List<Integer> temp = (List<Integer>) room.getValue();
                                    System.out.println(temp.toString());
                                    temp.set(1,temp.get(1) + r.getPrice());
                                    System.out.println("this date " + outputDateFormat.format(d) + " using rate " + r.toString());
                                    rooms.put(r.getRoomType(), temp);
                                    prioritySet = true;
                                    break;
                                }
                            }                
                        }

                    }
                }
                if(prioritySet != true) {
                    for(Rates r : normalRates){
                        if(room.getKey().equals(r.getRoomType())){
                            if(r.getRateType().equals("Normal")){ 
                                List<Integer> temp = (List<Integer>) room.getValue();
                                System.out.println(temp.toString());
                                temp.set(1,temp.get(1) + r.getPrice());
                                System.out.println("this date " + outputDateFormat.format(d) + " using rate " + r.toString());
                                rooms.put(r.getRoomType(), temp);
                                prioritySet = true;
                                break;
                                
                            }                
                        }

                    }
                }
            }
            
            gcal.add(Calendar.DAY_OF_MONTH,1);
        }
        //Loop through each date and check if date present in promo, then peak, else normal
//        for(LocalDate date = checkInDate.toInstant().(); date.isBefore(checkOutDate.toInstant()); date = date.plusDays(1)){
//        
//        }
    }
    
    @Override
    public boolean isWithinRange(Date date, Date startDate, Date endDate){
        return !(date.before(startDate) || date.after(endDate));
    }
    
    @Override
    public List<Reservations> addReservation(String roomType, Integer quantity) throws InvalidRoomTypeException, InvalidRoomQuantityException{
        if(availability.containsKey(roomType)){
            if(availability.get(roomType).get(0) >= quantity){
               List<Integer> newList = new ArrayList<>();
               Integer qty = availability.get(roomType).get(0);
                //need to update quantity also
                for(int i = 0; i < quantity; i++){
                    Reservations newReservation = new Reservations(new Customers(1l,"wd"), roomType, checkInDate, checkOutDate);              
                    reservations.add(newReservation);
                    qty -=1; 

                }
                newList.add(qty);
                newList.add(availability.get(roomType).get(1));
                newList.add(availability.get(roomType).get(2));
                availability.put(roomType, newList);

            }
            else {
                throw new InvalidRoomQuantityException("Invalid Quantity for " + roomType + " Only " + availability.get(roomType).get(0) + " rooms available");

                //throw invalid quantity
            }

        }
        else{
            throw new InvalidRoomTypeException("Invalid Room Type: " + roomType);
            //System.out.println("INVALID ROOMTYPE");
            //throw invalid roomtype
        }
        
        return reservations;
    }
    
    public List<Reservations> confirmReservations(){
        for(Reservations r: reservations){
            reservationsEntitySessionBeanLocal.createNewReservation(r);
        }
    
        return reservations;
    }
}
