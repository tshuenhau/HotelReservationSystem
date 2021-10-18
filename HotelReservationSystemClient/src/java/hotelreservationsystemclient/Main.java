/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelreservationsystemclient;

import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import entity.Employees;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author chenx
 */
public class Main {

    @EJB
    private static EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        List<Employees> employees = employeeEntitySessionBeanRemote.retrieveAllEmployees();
        
        for(Employees employee:employees) {
            System.out.println("Employee: username" + employee.getUsername());
        }
    }
    
}
