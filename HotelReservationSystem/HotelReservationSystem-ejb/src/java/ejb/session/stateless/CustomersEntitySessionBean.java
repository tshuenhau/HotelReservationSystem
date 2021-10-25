/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customers;
import entity.Employees;
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
public class CustomersEntitySessionBean implements CustomersEntitySessionBeanRemote, CustomersEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public List<Customers> retrieveAllCustomers() {
        Query query = em.createQuery("Select e FROM Customers e");
        
        return query.getResultList();
    }
    
    @Override
    public Long createNewCustomer(Customers newCustomer) {
        em.persist(newCustomer);
        
        return newCustomer.getPassportNum();
    }
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
