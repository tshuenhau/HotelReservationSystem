/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employees;
import java.util.List;
import javax.ejb.Local;
import util.exception.InvalidLoginCredentialException;
import util.exception.UserAlreadyExistException;

/**
 *
 * @author chenx
 */
@Local
public interface EmployeeEntitySessionBeanLocal {

    public List<Employees> retrieveAllEmployees();

    public String createNewEmployee(Employees newEmployee) throws UserAlreadyExistException;

    public Employees login(String username, String password) throws InvalidLoginCredentialException;
    
}
