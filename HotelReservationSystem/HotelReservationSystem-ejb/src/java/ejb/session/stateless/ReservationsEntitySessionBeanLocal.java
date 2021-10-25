/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reservations;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author tshuenhau
 */
@Local
public interface ReservationsEntitySessionBeanLocal {

    public List<Reservations> retrieveAllReservations();

    public Long createNewReservation(Reservations newReservation);
    
}
