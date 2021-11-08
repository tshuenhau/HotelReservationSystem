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
    public String createNewRoomType(RoomTypes roomType){
        em.persist(roomType);
        em.flush();
        return roomType.getRoomTypeName();
        
    }

    
}
