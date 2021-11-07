/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customers;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
    public Customers createNewCustomer(Customers newCustomer) throws UserAlreadyExistException {
        if(em.find(Customers.class, newCustomer.getPassportNum()) == null){
            em.persist(newCustomer);
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


}
