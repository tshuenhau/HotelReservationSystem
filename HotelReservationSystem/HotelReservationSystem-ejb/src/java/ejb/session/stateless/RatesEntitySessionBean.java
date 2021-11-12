/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Rates;
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
public class RatesEntitySessionBean implements RatesEntitySessionBeanRemote, RatesEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public List<Rates> retrieveAllRates(){
        Query query = em.createQuery("SELECT r FROM Rates r");
        return query.getResultList();
    }
    
    @Override
    public Rates createNewRate(Rates newRoomRate) {
            em.persist(newRoomRate);
            em.flush();
        return newRoomRate;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
