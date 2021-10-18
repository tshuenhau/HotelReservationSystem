/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelreservationsystemclient;

import ejb.session.stateless.HotelRoomsEntitySessionBeanRemote;
import entity.Employees;
import entity.HotelRooms;
import java.util.List;
import javax.ejb.EJB;
import ejb.session.stateless.EmployeesEntitySessionBeanRemote;

/**
 *
 * @author chenx
 */
public class Main {

    @EJB
    private static HotelRoomsEntitySessionBeanRemote hotelRoomsEntitySessionBeanRemote;

    @EJB
    private static EmployeesEntitySessionBeanRemote employeeEntitySessionBeanRemote;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        List<Employees> employees = employeeEntitySessionBeanRemote.retrieveAllEmployees();
        List<HotelRooms> hotelRooms = hotelRoomsEntitySessionBeanRemote.retrieveAllHotelRooms();

        for(Employees employee:employees) {
            System.out.println("Employee username: " + employee.getUsername());
        }
        
        for(HotelRooms h: hotelRooms){
            System.out.println("Hotel Room ID: " + h.getHotelRoomID());
        }
    }
    
}
