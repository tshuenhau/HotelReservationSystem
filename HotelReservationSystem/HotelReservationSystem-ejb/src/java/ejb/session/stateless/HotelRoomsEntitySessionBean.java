/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.HotelRooms;
import entity.RoomTypes;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.DeleteRoomException;
import util.exception.RoomAlreadyExistException;
import util.exception.RoomNotFoundException;

/**
 *
 * @author tshuenhau
 */
@Stateless
public class HotelRoomsEntitySessionBean implements HotelRoomsEntitySessionBeanRemote, HotelRoomsEntitySessionBeanLocal {

    @EJB
    private RoomTypesEntitySessionBeanLocal roomTypesEntitySessionBeanLocal;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    

    @Override
    public List<HotelRooms> retrieveAllHotelRooms(){
        Query query = em.createQuery("SELECT h FROM HotelRooms h");
        return query.getResultList();
    }
    
    @Override
    public String createNewHotelRoom(HotelRooms newHotelRoom) throws RoomAlreadyExistException {
        
        List<HotelRooms> hotelRooms = retrieveAllHotelRooms();
        boolean exists = false;

        if (newHotelRoom != null) {
            for (HotelRooms hotelRoom : hotelRooms) {
                if (hotelRoom.equals(newHotelRoom)) {
                    exists = true;
                    throw new RoomAlreadyExistException("Room Type Name " + hotelRoom.getHotelRoomID() + " already exists!");
                }
            }
        }
        if(em.find(HotelRooms.class, newHotelRoom.getHotelRoomID()) == null) {
            em.persist(newHotelRoom);
            em.flush();
        }
        return newHotelRoom.getHotelRoomID();
    }
    
    @Override
    public HotelRooms retrievesHotelRoomByRoomNumber(String roomNumber) throws RoomNotFoundException {
        List<HotelRooms> hotelRooms = retrieveAllHotelRooms();
        
        HotelRooms roomEntity = new HotelRooms();
        
        if (roomNumber != null) {
            for (HotelRooms hotelRoom : hotelRooms) {
                if (hotelRoom.getHotelRoomID().equals(roomNumber)) {
                    roomEntity = hotelRoom;
                }
            }
        }
        
        if(roomEntity != null){
            return roomEntity;
        }
        else {
            throw new RoomNotFoundException("Room Number " + roomNumber + " does not exist!");
        }
    }
    
    
    @Override
    public void updateHotelRoom(HotelRooms hotelRoom) throws RoomNotFoundException {
        if (hotelRoom != null && hotelRoom.getHotelRoomID() != null) {
            HotelRooms hotelRoomToUpdate = retrievesHotelRoomByRoomNumber(hotelRoom.getHotelRoomID());
            hotelRoomToUpdate.setStatus(false);
            em.merge(hotelRoomToUpdate);
            em.flush();
        } else {
            throw new RoomNotFoundException("Room " + hotelRoom.getHotelRoomID() + " cannot be found!");
        }
    }
    
    @Override
    public void deleteRoom(String roomNumber) throws RoomNotFoundException, DeleteRoomException {
        HotelRooms roomEntityToRemove = retrievesHotelRoomByRoomNumber(roomNumber);
        List<HotelRooms> roomEntities = retrieveAllHotelRooms();
        
        RoomTypes roomType = roomEntityToRemove.getRmType();
        List<HotelRooms> hotelRoomsLinked = roomType.getHotelRooms();
        List<HotelRooms> hotels = new ArrayList<>();        
        
        if(roomEntityToRemove.getIsAllocated() == false){
         
            em.remove(roomEntityToRemove);
        }
        else {
            throw new DeleteRoomException("Room " + roomNumber + " cannot be deleted since it is already allocated to a reservation!");
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
