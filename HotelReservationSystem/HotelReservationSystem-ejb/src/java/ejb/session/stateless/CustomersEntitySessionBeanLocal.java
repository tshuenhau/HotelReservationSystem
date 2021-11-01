/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customers;
import entity.Employees;
import java.util.List;
import javax.ejb.Local;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author tshuenhau
 */
@Local
public interface CustomersEntitySessionBeanLocal {

    public List<Customers> retrieveAllCustomers();

    public Long createNewCustomer(Customers newCustomer);


    public Customers login(Long passportNumber, String password) throws InvalidLoginCredentialException;
    
}
