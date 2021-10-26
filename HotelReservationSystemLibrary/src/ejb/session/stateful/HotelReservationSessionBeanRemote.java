/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import entity.HotelRooms;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author tshuenhau
 */
@Remote
public interface HotelReservationSessionBeanRemote {

    public void remove();
    
    public List<HotelRooms> searchHotelRooms(Date checkInDate, Date checkOutDate);
    
}
