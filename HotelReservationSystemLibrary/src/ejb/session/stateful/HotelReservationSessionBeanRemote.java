/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import entity.Customers;
import entity.Reservations;
import entity.RoomTypes;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;
import util.exception.InvalidRoomQuantityException;
import util.exception.InvalidRoomTypeException;
import util.exception.NotLoggedInException;

/**
 *
 * @author tshuenhau
 */
@Remote
public interface HotelReservationSessionBeanRemote {

    public void remove();
    
    public Map<RoomTypes, List<Integer>> searchHotelRooms(Date checkInDate, Date checkOutDate);

    public List<Reservations> addReservation(String inputRoomType, Integer quantity) throws InvalidRoomTypeException, InvalidRoomQuantityException, NotLoggedInException;

    public boolean isWithinRange(Date date, Date startDate, Date endDate);

    public List<Reservations> confirmReservations();

    public Map<RoomTypes, Integer> getRoomQuantities();

    public Integer getTotalCost();

    public void login(Customers customer);

    public Map<RoomTypes, List<Integer>> walkInSearchHotelRooms(Date checkInDate, Date checkOutDate);

    public Customers checkLoggedIn() throws NotLoggedInException;

    public void setCurrentCustomer(Customers currentCustomer);

    public Customers getCurrentCustomer();

    public Boolean isLoggedIn();
    
}
