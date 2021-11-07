/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import classes.Allocation;
import entity.Customers;
import entity.Reservations;
import java.util.ArrayList;
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
public class ReservationsEntitySessionBean implements ReservationsEntitySessionBeanRemote, ReservationsEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    


    @Override
    public List<Reservations> retrieveAllReservations() {
        Query query = em.createQuery("Select e FROM Reservations e");   
        return query.getResultList();
    }
    
    @Override
    public List<Reservations> retrieveReserationsOfCustomer(Customers customer){
        Query query = em.createQuery("Select e FROM Reservations e WHERE e.reservedBy = :value", Reservations.class).setParameter("value", customer);
        return query.getResultList();
    }
    
    @Override
    public Long createNewReservation(Reservations newReservation) {
        em.persist(newReservation);
                em.flush();

        return newReservation.getReservationID();
    }
    
    @Override
    public Reservations updateReservation(Reservations reservation){
        em.merge(reservation);
        em.flush();
        return reservation;
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
