/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.HotelRooms;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeleteRoomException;
import util.exception.RoomAlreadyExistException;
import util.exception.RoomNotFoundException;

/**
 *
 * @author tshuenhau
 */
@Local
public interface HotelRoomsEntitySessionBeanLocal {

    public List<HotelRooms> retrieveAllHotelRooms();

    public String createNewHotelRoom(HotelRooms hotelRoom) throws RoomAlreadyExistException;

    public HotelRooms retrievesHotelRoomByRoomNumber(String roomNumber) throws RoomNotFoundException;

    public void updateHotelRoom(HotelRooms hotelRoom) throws RoomNotFoundException;

    public void deleteRoom(String roomNumber) throws RoomNotFoundException, DeleteRoomException;
    
}
