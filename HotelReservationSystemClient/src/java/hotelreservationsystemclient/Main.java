/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelreservationsystemclient;

import ejb.session.stateless.CustomersEntitySessionBeanRemote;
import ejb.session.stateless.HotelRoomsEntitySessionBeanRemote;
import entity.Employees;
import entity.HotelRooms;
import java.util.List;
import javax.ejb.EJB;
import ejb.session.stateless.EmployeesEntitySessionBeanRemote;
import ejb.session.stateless.RatesEntitySessionBeanRemote;
import ejb.session.stateless.ReservationsEntitySessionBeanRemote;
import entity.Customers;
import entity.Rates;
import entity.Reservations;

/**
 *
 * @author chenx
 */
public class Main {

    @EJB
    private static ReservationsEntitySessionBeanRemote reservationsEntitySessionBeanRemote;

    @EJB
    private static CustomersEntitySessionBeanRemote customersEntitySessionBeanRemote;

    @EJB
    private static RatesEntitySessionBeanRemote ratesEntitySessionBeanRemote;

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
        List<Rates> rates = ratesEntitySessionBeanRemote.retrieveAllRates();
        List<Customers> customers = customersEntitySessionBeanRemote.retrieveAllCustomers();
        List<Reservations> reservations = reservationsEntitySessionBeanRemote.retrieveAllReservations();
        
        for(Employees employee:employees) {
            System.out.println("Employee username: " + employee.getUsername());
        }
        
        for(HotelRooms h: hotelRooms){
            System.out.println("Hotel Room ID: " + h.getHotelRoomID());
        }
        
        for(Rates r: rates){
            System.out.println("Rate ID: " + r.getRateID() + "Rate Type: " + r.getRateType());
        }
        
        /*
        for(Customers c: customers){
            System.out.println("Customer passport: " + c.getPassportNum() + "Password: " + c.getPassword());
        }
        
        for(Reservations r: reservations){
            System.out.println("Reservation ID: " + r.getReservationID() + "Room Type: " + r.getRoomType());
        }
        */
    }
    
}
