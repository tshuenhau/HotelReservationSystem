/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customers;
import entity.Reservations;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CheckOutException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UserAlreadyExistException;

/**
 *
 * @author tshuenhau
 */
@Stateless
public class CustomersEntitySessionBean implements CustomersEntitySessionBeanRemote, CustomersEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public List<Customers> retrieveAllCustomers() {
        Query query = em.createQuery("Select e FROM Customers e");
        
        return query.getResultList();
    }
    
    @Override
    public List<Customers> retrieveAllPartners() {
        Query query = em.createQuery("Select e FROM Customers e WHERE e.isPartner = true");
        
        return query.getResultList();
    }
    
    @Override
    public Customers createNewCustomer(Customers newCustomer) throws UserAlreadyExistException {
        if(em.find(Customers.class, newCustomer.getPassportNum()) == null){
            em.persist(newCustomer);
            em.flush();
            return newCustomer;
        }
        throw new UserAlreadyExistException();
    }
    
    @Override
    public Customers login(Long passportNumber, String password) throws InvalidLoginCredentialException {
        List<Customers> customers = retrieveAllCustomers();
        
        for(Customers customer:customers)
        {
            System.out.println(customer.getPassportNum());
            if(customer.getPassportNum().equals(passportNumber) && customer.getPassword().equals(password))
            {
                return customer;
            }
        }
        
        throw new InvalidLoginCredentialException("Invalid login credential");
    }
    
    @Override
    public void checkOut(Long passportNum, Long reservationID) throws CheckOutException, CustomerNotFoundException {
        Customers customerEntity = retrievesCustomerByPassportNum(passportNum);
        List<Reservations> reservations = customerEntity.getReservations();
        
        Reservations reservationToCheckOut = new Reservations();
        
        if(customerEntity.getReservations() != null){
            for (Reservations reservation : reservations) {
                if (reservation.getReservationID() == reservationID) {
                    reservationToCheckOut = reservation;
                }
            }
            em.remove(reservationToCheckOut);
        }
        else {
            throw new CheckOutException("Customer " + passportNum + " cannot be checked out!");
        }
    }
    
    @Override
    public Customers retrievesCustomerByPassportNum(Long passportNum) throws CustomerNotFoundException {
        List<Customers> customers = retrieveAllCustomers();
        
        Customers customerEntity = new Customers();
        
        if (passportNum != null) {
            for (Customers customer : customers) {
                if (customer.getPassportNum().equals(passportNum)) {
                    customerEntity = customer;
                }
            }
        }
        
        if(customerEntity != null){
            return customerEntity;
        }
        else {
            throw new CustomerNotFoundException("Customer " + passportNum + " does not exist!");
        }
    }


}
