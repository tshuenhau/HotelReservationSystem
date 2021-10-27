/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import ejb.session.stateless.HotelRoomsEntitySessionBeanLocal;
import ejb.session.stateless.ReservationsEntitySessionBeanLocal;
import entity.HotelRooms;
import entity.Rates;
import entity.Reservations;
import java.math.BigDecimal;
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
    private ReservationsEntitySessionBeanLocal reservationsEntitySessionBeanLocal;

    @EJB
    private HotelRoomsEntitySessionBeanLocal hotelRoomsEntitySessionBeanLocal;
    
    Date checkInDate;
    Date checkOutDate;
    private BigDecimal totalAmount;
    private List<HotelRooms> hotelRooms;
    private List<Rates> rates;
    
    
    /*
    Room type -- Inventory -- Reservations
    
    
    */
    private Map<String,List<Integer>> availability = new HashMap();
    
    @Remove
    @Override
    public void remove()
    {
        // Do nothing
    }
    @PreDestroy
    public void preDestroy()
    {
        if(hotelRooms != null)
        {
            hotelRooms.clear();
        availability.clear();

            hotelRooms = null;
        }
    }
    @Override
    public Map<String,List<Integer>> searchHotelRooms(Date checkInDate, Date checkOutDate){
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        hotelRooms = new ArrayList<HotelRooms>();
        availability.put("Deluxe Room", Arrays.asList(0,0));
        availability.put("Premier Room", Arrays.asList(0,0));
        availability.put("Family Room", Arrays.asList(0,0));
        availability.put("Junior Suite", Arrays.asList(0,0));
        availability.put("Grand Suite", Arrays.asList(0,0));

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
            //if(hotelRoom.getStatus() == false){continue;}
            List<Integer> currList = new ArrayList<Integer>();

            if(availability.containsKey(reservation.getRoomType())){
                currList = availability.get(reservation.getRoomType());
            }
            List<Integer> newList = new ArrayList<Integer>();
            newList.add(currList.get(0));
            newList.add(currList.get(1) + 1);
            availability.put(reservation.getRoomType(), newList);
        }
        
//        for(Reservations r:reservations){
//            System.out.println(r);
//        }
        //Get ALL THE RESERVATIONS BETWEEN DATE RANGE
        //THEN do a count of hotel room type in allHotelRooms and reservations then only display the excess hotel rooms.

        
        return availability; //! REMEMBER TO CHANGE THIS
    
    }
}
