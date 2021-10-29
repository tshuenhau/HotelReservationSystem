/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CustomersEntitySessionBeanLocal;
import ejb.session.stateless.EmployeeEntitySessionBeanLocal;
import ejb.session.stateless.HotelRoomsEntitySessionBeanLocal;
import ejb.session.stateless.RatesEntitySessionBeanLocal;
import ejb.session.stateless.ReservationsEntitySessionBeanLocal;
import entity.Rates;
import entity.Employees;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author chenx
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private ReservationsEntitySessionBeanLocal reservationsEntitySessionBeanLocal;

    @EJB
    private CustomersEntitySessionBeanLocal customersEntitySessionBeanLocal;

    @EJB
    private RatesEntitySessionBeanLocal ratesEntitySessionBeanLocal;

    @EJB
    private HotelRoomsEntitySessionBeanLocal hotelRoomsEntitySessionBeanLocal;

    @EJB
    private EmployeeEntitySessionBeanLocal employeeEntitySessionBeanLocal;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    

    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @PostConstruct
    public void postConstruct() {
        
//        ratesEntitySessionBeanLocal.createNewRate(new Rates("Deluxe Room","Published", 10));
//        ratesEntitySessionBeanLocal.createNewRate(new Rates("Deluxe Room","Normal", 11));
//        ratesEntitySessionBeanLocal.createNewRate(new Rates("Deluxe Room","Peak", 12));
//        ratesEntitySessionBeanLocal.createNewRate(new Rates("Deluxe Room","Promo", 9));
//
//            ratesEntitySessionBeanLocal.createNewRate(new Rates("Premier Room","Published", 11));
//        ratesEntitySessionBeanLocal.createNewRate(new Rates("Premier Room","Normal", 12));
//        ratesEntitySessionBeanLocal.createNewRate(new Rates("Premier Room","Peak", 13));
//        ratesEntitySessionBeanLocal.createNewRate(new Rates("Premier Room","Promo", 10));
//
//                ratesEntitySessionBeanLocal.createNewRate(new Rates("Family Room","Published", 12));
//        ratesEntitySessionBeanLocal.createNewRate(new Rates("Family Room","Normal", 13));
//        ratesEntitySessionBeanLocal.createNewRate(new Rates("Family Room","Peak", 14));
//        ratesEntitySessionBeanLocal.createNewRate(new Rates("Family Room","Promo", 11));
//
//                ratesEntitySessionBeanLocal.createNewRate(new Rates("Junior Suite","Published", 13));
//        ratesEntitySessionBeanLocal.createNewRate(new Rates("Junior Suite","Normal", 14));
//        ratesEntitySessionBeanLocal.createNewRate(new Rates("Junior Suite","Peak", 15));
//        ratesEntitySessionBeanLocal.createNewRate(new Rates("Junior Suite","Promo", 12));
//
//                ratesEntitySessionBeanLocal.createNewRate(new Rates("Grand Suite","Published", 14));
//        ratesEntitySessionBeanLocal.createNewRate(new Rates("Grand Suite","Normal", 15));
//        ratesEntitySessionBeanLocal.createNewRate(new Rates("Grand Suite","Peak", 16));
//        ratesEntitySessionBeanLocal.createNewRate(new Rates("Grand Suite","Promo", 13));

        
//                hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("Deluxe Room"));
//                hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("Deluxe Room"));
//                hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("Premier Room"));
//                hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("Premier Room"));
//                hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("Premier Room"));
//                hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("Family Room"));
//                hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("Grand Suite"));

        /*
        if(em.find(Employees.class, "employees1") == null){
            employeeEntitySessionBeanLocal.createNewEmployee(new Employees("employee1", "Sales Manager", "123"));
            employeeEntitySessionBeanLocal.createNewEmployee(new Employees("employee2", "Sales Manager", "123"));
            employeeEntitySessionBeanLocal.createNewEmployee(new Employees("employee3", "Sales Manager", "123"));
        }
        
        
        hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms(1l, "Sales Manager", false));
        hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms(2l, "Sales Manager", false));
        hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms(3l, "Sales Manager", false));

        
        */
        
     
        //ratesEntitySessionBeanLocal.createNewRate(new Rates("Deluxe Room","Published", 5));
        //ratesEntitySessionBeanLocal.createNewRate(new Rates("Deluxe Room","Published", 9));
        //ratesEntitySessionBeanLocal.createNewRate(new Rates("Deluxe Room","Published", 1));
  
//        Customers c1= new Customers(1l,"hello");
//        Customers c2= new Customers(2l,"hello2");

//        customersEntitySessionBeanLocal.createNewCustomer(c1);
//        customersEntitySessionBeanLocal.createNewCustomer(c2);

        //reservationsEntitySessionBeanLocal.createNewReservation(new Reservations(c1, "Deluxe Room", new Date(), new Date()));
        
        //    public Reservations(Customers reservedBy, String roomType, Date startDate, Date endDate) {

        //Customers c1= new Customers(1l,"hello");
        //reservationsEntitySessionBeanLocal.createNewReservation(new Reservations(c1, "Deluxe Room", new Date(1111,11,11), new Date(1111,11,11)));
        
        employeeEntitySessionBeanLocal.createNewEmployee(new Employees("employee4", "System Administrator", "123"));
    }
    
}
