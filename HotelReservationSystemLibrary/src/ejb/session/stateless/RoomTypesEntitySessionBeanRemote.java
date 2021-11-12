/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomTypes;
import java.util.List;
import javax.ejb.Remote;
import util.exception.DeleteRoomTypeException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author chenx
 */
@Remote
public interface RoomTypesEntitySessionBeanRemote {
    public List<RoomTypes> retrieveAllRoomTypes();
    public RoomTypes createNewRoomType(RoomTypes roomType);
    
    public void deleteRoomType(String roomTypeName) throws RoomTypeNotFoundException, DeleteRoomTypeException;
    public RoomTypes retrievesRoomTypeByRoomTypeName(String roomTypeName) throws RoomTypeNotFoundException;
}
