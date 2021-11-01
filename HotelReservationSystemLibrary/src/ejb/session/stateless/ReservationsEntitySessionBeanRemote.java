/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customers;
import entity.Reservations;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author tshuenhau
 */
@Remote
public interface ReservationsEntitySessionBeanRemote {
    public List<Reservations> retrieveAllReservations();
    public List<Reservations> retrieveReserationsOfCustomer(Customers customer);

    public Long createNewReservation(Reservations newReservation);  
}
