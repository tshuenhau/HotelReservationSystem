/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reservations;
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
public class ReservationsEntitySessionBean implements ReservationsEntitySessionBeanRemote, ReservationsEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public List<Reservations> retrieveAllReservations() {
        Query query = em.createQuery("Select r FROM Reservations r");
        
        return query.getResultList();
    }
    
    @Override
    public Long createNewReservation(Reservations newReservation) {
        em.persist(newReservation);
        
        return newReservation.getReservationID();
    }
    
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
