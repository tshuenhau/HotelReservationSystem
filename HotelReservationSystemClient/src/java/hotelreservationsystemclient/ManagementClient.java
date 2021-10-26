/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelreservationsystemclient;

import ejb.session.stateless.CustomersEntitySessionBeanRemote;
import ejb.session.stateless.EmployeesEntitySessionBeanRemote;
import ejb.session.stateless.HotelRoomsEntitySessionBeanRemote;
import ejb.session.stateless.RatesEntitySessionBeanRemote;
import ejb.session.stateless.ReservationsEntitySessionBeanRemote;
import entity.Customers;
import entity.Employees;
import entity.HotelRooms;
import entity.Rates;
import entity.Reservations;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author tshuenhau
 */
public class ManagementClient {
    
    private ReservationsEntitySessionBeanRemote reservationsEntitySessionBeanRemote;

    private CustomersEntitySessionBeanRemote customersEntitySessionBeanRemote;

    private RatesEntitySessionBeanRemote ratesEntitySessionBeanRemote;

    private HotelRoomsEntitySessionBeanRemote hotelRoomsEntitySessionBeanRemote;

    private EmployeesEntitySessionBeanRemote employeeEntitySessionBeanRemote;

    public ManagementClient(ReservationsEntitySessionBeanRemote reservationsEntitySessionBeanRemote, CustomersEntitySessionBeanRemote customersEntitySessionBeanRemote, RatesEntitySessionBeanRemote ratesEntitySessionBeanRemote, HotelRoomsEntitySessionBeanRemote hotelRoomsEntitySessionBeanRemote, EmployeesEntitySessionBeanRemote employeeEntitySessionBeanRemote) {
        this.reservationsEntitySessionBeanRemote = reservationsEntitySessionBeanRemote;
        this.customersEntitySessionBeanRemote = customersEntitySessionBeanRemote;
        this.ratesEntitySessionBeanRemote = ratesEntitySessionBeanRemote;
        this.hotelRoomsEntitySessionBeanRemote = hotelRoomsEntitySessionBeanRemote;
        this.employeeEntitySessionBeanRemote = employeeEntitySessionBeanRemote;
    }
    
    
    public void runApp(){
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
        
        for(Customers c: customers){
            System.out.println("passportNum: " + c.getPassportNum());
        }
        
        for(Reservations r: reservations) {
                 System.out.println("reservationID: " + r.getReservationID());
       
        }
    }
}
