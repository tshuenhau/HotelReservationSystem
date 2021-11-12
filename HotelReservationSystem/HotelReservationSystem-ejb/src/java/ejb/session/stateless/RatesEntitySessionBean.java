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
import util.exception.RateAlreadyExistException;
import util.exception.RatesNotFoundException;
import util.exception.RoomTypeNotFoundException;

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
    public Rates createNewRate(Rates newRoomRate) throws RateAlreadyExistException {
        List<Rates> rates = retrieveAllRates();
        boolean exists = false;

        if (newRoomRate != null) {
            for (Rates rate : rates) {
                if (rate.equals(newRoomRate)) {
                    exists = true;
                    throw new RateAlreadyExistException("Rate " + rate.getRateType() + rate.getRoomType() + " already exists!");
                }
            }
        }
        
        if (!exists) {
            em.persist(newRoomRate);
            em.flush();
        }
        return newRoomRate;
    }
    
    @Override
    public Rates retrievesRatesByRateID(Long rateID) throws RatesNotFoundException {
        List<Rates> rates = retrieveAllRates();
        
        Rates ratesEntity = new Rates();
        
        if (rateID != null) {
            for (Rates rate : rates) {
                if (rate.getRateID().equals(rateID)) {
                    ratesEntity = rate;
                }
            }
        }
        
        if(ratesEntity != null){
            return ratesEntity;
        }
        else {
            throw new RatesNotFoundException("Rate " + rateID + " does not exist!");
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
