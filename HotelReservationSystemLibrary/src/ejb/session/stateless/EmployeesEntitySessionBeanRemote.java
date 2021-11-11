/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employees;
import java.util.List;
import javax.ejb.Remote;
import util.exception.InvalidLoginCredentialException;
import util.exception.UserAlreadyExistException;

/**
 *
 * @author chenx
 */
@Remote
public interface EmployeesEntitySessionBeanRemote {
    
    public List<Employees> retrieveAllEmployees();
    
    public String createNewEmployee(Employees newEmployee);
    
    public Employees login(String username, String password) throws InvalidLoginCredentialException;

}
