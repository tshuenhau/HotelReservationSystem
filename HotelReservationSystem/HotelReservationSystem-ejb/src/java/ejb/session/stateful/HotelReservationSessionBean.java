/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import ejb.session.stateless.HotelRoomsEntitySessionBeanLocal;
import ejb.session.stateless.RatesEntitySessionBeanLocal;
import ejb.session.stateless.ReservationsEntitySessionBeanLocal;
import entity.HotelRooms;
import entity.Rates;
import entity.Reservations;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;

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
    
    Date checkInDate;
    Date checkOutDate;
    Integer dayCount;
    private List<Rates> rates;
    private Map<String,List<Integer>> availability = new HashMap();
    private Map<String,List<Integer>> rooms = new HashMap();//room type -- availability -- cost

    
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
        availability.put("Deluxe Room", Arrays.asList(0,0,12));
        availability.put("Premier Room", Arrays.asList(0,0,12));
        availability.put("Family Room", Arrays.asList(0,0,12));
        availability.put("Junior Suite", Arrays.asList(0,0,10));
        availability.put("Grand Suite", Arrays.asList(0,0,10));

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
        List<Reservations> reservations = reservationsEntitySessionBeanLocal.retrieveAllReservations();
        List<Reservations> toRemove = new ArrayList<Reservations>();
        for(Reservations r: reservations){
            if(r.getStartDate().compareTo(checkOutDate) >= 0){
                toRemove.add(r);
            }
            else if(r.getEndDate().compareTo(checkInDate) <= 0){
                toRemove.add(r);
            }
        }
        reservations.removeAll(toRemove);
        
        for(Reservations reservation : reservations){
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
        System.out.println("Calculate  ");

        rates = ratesEntitySessionBeanLocal.retrieveAllRates();
        List<Rates> relaventRates = new ArrayList();
        for(Rates r : rates) {
//                    System.out.println("Iterating...  ");

            if(r.getRateType() == "Normal"){
                relaventRates.add(r);
            }
            else if(r.getRateType().equals("Peak") && r.getStartDate() != null && r.getEndDate() != null){
                                    System.out.println("Peak...  ");

                if(r.getStartDate().compareTo(checkOutDate) <= 0 && r.getEndDate().compareTo(checkInDate) >= 0){
                    if(r.getStartDate().compareTo(checkInDate) >= 0 && r.getEndDate().compareTo(checkOutDate) <=0){
                        //get time range from
                        Long time = ChronoUnit.DAYS.between(r.getStartDate().toInstant(),r.getEndDate().toInstant());
                        System.out.println("DAYS  " + time);
                    }
                }
            }
        }
    }
    
 
}
