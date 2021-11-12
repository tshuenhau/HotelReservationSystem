/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomTypes;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.DeleteRoomTypeException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author chenx
 */
@Stateless
public class RoomTypesEntitySessionBean implements RoomTypesEntitySessionBeanRemote, RoomTypesEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    
    @Override
    public List<RoomTypes> retrieveAllRoomTypes(){
        Query query = em.createQuery("SELECT r FROM RoomTypes r");
        return query.getResultList();
    }
    
    @Override
    public RoomTypes createNewRoomType(RoomTypes newRoomType){
            em.persist(newRoomType);
            em.flush();
        return newRoomType;
    }
    
    
    @Override
    public RoomTypes retrievesRoomTypeByRoomTypeName(String roomTypeName) throws RoomTypeNotFoundException {
        List<RoomTypes> roomTypes = retrieveAllRoomTypes();
        
        RoomTypes roomTypeEntity = new RoomTypes();
        
        if (roomTypeName != null) {
            for (RoomTypes roomType : roomTypes) {
                if (roomType.getRoomTypeName().equals(roomTypeName)) {
                    roomTypeEntity = roomType;
                }
            }
        }
        
        if(roomTypeEntity != null){
            return roomTypeEntity;
        }
        else
        {
            throw new RoomTypeNotFoundException("Room Type Name " + roomTypeName + " does not exist!");
        }
    }
    
    @Override
    public void updateRoomType(RoomTypes roomType, String newRoomTypeName) throws RoomTypeNotFoundException {
        if (roomType != null && roomType.getRoomTypeName() != null) {
            RoomTypes roomTypeToUpdate = retrievesRoomTypeByRoomTypeName(roomType.getRoomTypeName());
            roomTypeToUpdate.setRoomTypeName(newRoomTypeName);
            em.merge(roomTypeToUpdate);
            em.flush();
        } else {
            throw new RoomTypeNotFoundException("Room Type " + roomType.getRoomTypeName() + " cannot be found!");
        }
    }
    
    @Override
    public void deleteRoomType(String roomTypeName) throws RoomTypeNotFoundException, DeleteRoomTypeException {
        RoomTypes roomTypeEntityToRemove = retrievesRoomTypeByRoomTypeName(roomTypeName);
        List<RoomTypes> roomTypesEntities = retrieveAllRoomTypes();
        
        
        if(roomTypeEntityToRemove.getHotelRooms().isEmpty() && roomTypeEntityToRemove.getReservations().isEmpty() && roomTypeEntityToRemove.getRoomRates().isEmpty()){
            for (RoomTypes roomTypes : roomTypesEntities) {
                if (roomTypes.getNextHigherRoomType().getRoomTypeName().equals(roomTypeName)) {
                    roomTypes.setNextHigherRoomType(null);
                } else if (roomTypes.getNextHigherRoomType() == null){
                    //do nothing
                }
            }
            em.remove(roomTypeEntityToRemove);
            System.out.println("Room Type successfully removed!");
        }
        else
        {
            throw new DeleteRoomTypeException("Room Type " + roomTypeName + " is associated with existing hotel rooms and/or reservations and/or room rates and cannot be deleted!");
        }
    }

    
}
