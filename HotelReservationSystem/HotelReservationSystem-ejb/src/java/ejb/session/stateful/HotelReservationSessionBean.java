/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import ejb.session.stateless.HotelRoomsEntitySessionBeanLocal;
import entity.HotelRooms;
import entity.Reservations;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    private HotelRoomsEntitySessionBeanLocal hotelRoomsEntitySessionBeanLocal;
    
    
    Date checkInDate;
    Date checkOutDate;
    private BigDecimal totalAmount;
    private List<HotelRooms> hotelRooms;
    
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
            hotelRooms = null;
        }
    }
    @Override
    public List<HotelRooms> searchHotelRooms(Date checkInDate, Date checkOutDate){
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        
        
        hotelRooms = new ArrayList<HotelRooms>();
        List<HotelRooms> allHotelRooms = hotelRoomsEntitySessionBeanLocal.retrieveAllHotelRooms();
        List<Reservations> reservations;//Get ALL THE RESERVATIONS BETWEEN DATE RANGE
        
        //THEN do a count of hotel room type in allHotelRooms and reservations then only display the excess hotel rooms.

        
        return allHotelRooms; //! REMEMBER TO CHANGE THIS
    
    }
}
