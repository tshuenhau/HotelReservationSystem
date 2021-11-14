/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customers;
import entity.Employees;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CheckOutException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UserAlreadyExistException;

/**
 *
 * @author tshuenhau
 */
@Remote
public interface CustomersEntitySessionBeanRemote {
    public List<Customers> retrieveAllCustomers();
    public List<Customers> retrieveAllPartners();
    public Customers login(Long passportNumber, String password) throws InvalidLoginCredentialException;

    public Customers retrievesCustomerByPassportNum(Long passportNum) throws CustomerNotFoundException;
    public Customers createNewCustomer(Customers newCustomer) throws UserAlreadyExistException;
    public void checkOut(Long passportNum, Long reservationID) throws CheckOutException, CustomerNotFoundException;
}
