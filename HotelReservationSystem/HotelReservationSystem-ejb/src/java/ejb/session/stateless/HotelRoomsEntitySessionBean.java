/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.HotelRooms;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author tshuenhau
 */
@Stateless
public class HotelRoomsEntitySessionBean implements HotelRoomsEntitySessionBeanRemote, HotelRoomsEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public List<HotelRooms> retrieveAllHotelRooms(){
        Query query = em.createQuery("SELECT h FROM HotelRooms h");
        return query.getResultList();
    }
    
    @Override
    public String createNewHotelRoom(HotelRooms hotelRoom){
        em.persist(hotelRoom);
        em.flush();
        return hotelRoom.getHotelRoomID();
        
    }
    


    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
