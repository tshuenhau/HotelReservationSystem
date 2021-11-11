/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employees;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.InvalidLoginCredentialException;
import util.exception.UserAlreadyExistException;

/**
 *
 * @author chenx
 */
@Stateless
public class EmployeeEntitySessionBean implements EmployeesEntitySessionBeanRemote, EmployeeEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public List<Employees> retrieveAllEmployees() {
        Query query = em.createQuery("Select e FROM Employees e");
        
        return query.getResultList();
    }
    
    @Override
    public String createNewEmployee(Employees newEmployee) throws UserAlreadyExistException {        
        if(em.find(Employees.class, newEmployee.getUsername()) == null){
            em.persist(newEmployee);
            em.flush();
            return newEmployee.getUsername();
        }
        throw new UserAlreadyExistException();
    }
    
    public Employees login(String username, String password) throws InvalidLoginCredentialException {
        List<Employees> employees = retrieveAllEmployees();
        
        for(Employees employee:employees){
            if(employee.getUsername().equals(username) && employee.getPassword().equals(password))
            {
                return employee;
            }
        }
        
        throw new InvalidLoginCredentialException("Invalid login credential");
    }
}
