/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employees;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author chenx
 */
@Remote
public interface EmployeeEntitySessionBeanRemote {
    
    public List<Employees> retrieveAllEmployees();
    
    public String createNewEmployee(Employees newEmployee);

}
