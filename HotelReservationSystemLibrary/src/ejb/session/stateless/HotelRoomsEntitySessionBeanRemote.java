/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.HotelRooms;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author tshuenhau
 */
@Remote
public interface HotelRoomsEntitySessionBeanRemote {
    public List<HotelRooms> retrieveAllHotelRooms();
    public String createNewHotelRoom(HotelRooms hotelRoom);


}
