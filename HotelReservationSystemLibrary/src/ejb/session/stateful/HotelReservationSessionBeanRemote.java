/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import entity.Customers;
import entity.Reservations;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;
import util.exception.InvalidRoomQuantityException;
import util.exception.InvalidRoomTypeException;

/**
 *
 * @author tshuenhau
 */
@Remote
public interface HotelReservationSessionBeanRemote {

    public void remove();
    
    public Map<String, List<Integer>> searchHotelRooms(Date checkInDate, Date checkOutDate);

    public List<Reservations> addReservation (String roomType, Integer quantity) throws InvalidRoomTypeException, InvalidRoomQuantityException;

    public boolean isWithinRange(Date date, Date startDate, Date endDate);

    public List<Reservations> confirmReservations();

    public Map<String, Integer> getRoomQuantities();

    public Integer getTotalCost();

    public void login(Customers customer);

    public Map<String, List<Integer>> walkInSearchHotelRooms(Date checkInDate, Date checkOutDate);
    
}
