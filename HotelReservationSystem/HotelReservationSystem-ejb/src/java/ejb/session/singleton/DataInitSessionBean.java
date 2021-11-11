/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateful.AllocationSessionBeanLocal;
import ejb.session.stateless.CustomersEntitySessionBeanLocal;
import ejb.session.stateless.EmployeeEntitySessionBeanLocal;
import ejb.session.stateless.HotelRoomsEntitySessionBeanLocal;
import ejb.session.stateless.RatesEntitySessionBeanLocal;
import ejb.session.stateless.ReservationsEntitySessionBeanLocal;
import ejb.session.stateless.RoomTypesEntitySessionBeanLocal;
import entity.Employees;
import entity.HotelRooms;
import entity.Rates;
import entity.RoomTypes;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private RoomTypesEntitySessionBeanLocal roomTypesEntitySessionBeanLocal;

    @EJB
    private AllocationSessionBeanLocal allocationSessionBeanLocal;

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

    SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");


    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @PostConstruct
    public void postConstruct() {
 
        employeeEntitySessionBeanLocal.createNewEmployee(new Employees("sysadmin", "System Administrator", "password"));
        employeeEntitySessionBeanLocal.createNewEmployee(new Employees("opmanager", "Operation Manager", "password"));
        employeeEntitySessionBeanLocal.createNewEmployee(new Employees("salesmanager", "Sales Manager", "password"));
        employeeEntitySessionBeanLocal.createNewEmployee(new Employees("guestrelo", "Guest Relation Officer", "password"));

        
        RoomTypes GrandSuite = new RoomTypes("Grand Suite", null);
        RoomTypes JuniorSuite = new RoomTypes("Junior Suite", GrandSuite);
        RoomTypes FamilyRoom = new RoomTypes("Family Room", JuniorSuite);
        RoomTypes PremierRoom = new RoomTypes("Premier Room", FamilyRoom);
        RoomTypes DeluxeRoom = new RoomTypes("Deluxe Room", PremierRoom);
        roomTypesEntitySessionBeanLocal.createNewRoomType(GrandSuite);
        roomTypesEntitySessionBeanLocal.createNewRoomType(JuniorSuite);
        roomTypesEntitySessionBeanLocal.createNewRoomType(FamilyRoom);
        roomTypesEntitySessionBeanLocal.createNewRoomType(PremierRoom);
        roomTypesEntitySessionBeanLocal.createNewRoomType(DeluxeRoom);

        
        ratesEntitySessionBeanLocal.createNewRate(new Rates(DeluxeRoom, "Published", 100));
        ratesEntitySessionBeanLocal.createNewRate(new Rates(DeluxeRoom, "Normal", 50));
        ratesEntitySessionBeanLocal.createNewRate(new Rates(PremierRoom, "Published", 200));
        ratesEntitySessionBeanLocal.createNewRate(new Rates(PremierRoom, "Normal", 100));
        ratesEntitySessionBeanLocal.createNewRate(new Rates(FamilyRoom, "Published", 300));
        ratesEntitySessionBeanLocal.createNewRate(new Rates(FamilyRoom, "Normal", 150));
        ratesEntitySessionBeanLocal.createNewRate(new Rates(JuniorSuite, "Published", 400));
        ratesEntitySessionBeanLocal.createNewRate(new Rates(JuniorSuite, "Normal", 200));
        ratesEntitySessionBeanLocal.createNewRate(new Rates(GrandSuite, "Published", 500));
        ratesEntitySessionBeanLocal.createNewRate(new Rates(GrandSuite, "Normal", 250));

        
        hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("0101", DeluxeRoom));
        hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("0201", DeluxeRoom));
        hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("0301", DeluxeRoom));
        hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("0401", DeluxeRoom));
        hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("0501", DeluxeRoom));
        hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("0102", PremierRoom));
        hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("0202", PremierRoom));
        hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("0302", PremierRoom));
        hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("0402", PremierRoom));
        hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("0502", PremierRoom));
        hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("0103", FamilyRoom));
        hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("0203", FamilyRoom));
        hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("0303", FamilyRoom));
        hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("0403", FamilyRoom));
        hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("0503", FamilyRoom));
        hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("0104", JuniorSuite));
        hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("0204", JuniorSuite));
        hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("0304", JuniorSuite));
        hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("0404", JuniorSuite));
        hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("0504", JuniorSuite));
        hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("0105", GrandSuite));
        hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("0205", GrandSuite));
        hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("0305", GrandSuite));
        hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("0405", GrandSuite));
        hotelRoomsEntitySessionBeanLocal.createNewHotelRoom(new HotelRooms("0505", GrandSuite));
        
            try {
            allocationSessionBeanLocal.generateReport(inputDateFormat.parse("16/11/1111"));
            //allocationSessionBeanLocal.allocateRooms(inputDateFormat.parse("11/11/1111"));
            } catch (ParseException ex) {
            Logger.getLogger(DataInitSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
