/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomTypes;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author chenx
 */
@Local
public interface RoomTypesEntitySessionBeanLocal {

    public List<RoomTypes> retrieveAllRoomTypes();

    public String createNewRoomType(RoomTypes roomType);
    
}
