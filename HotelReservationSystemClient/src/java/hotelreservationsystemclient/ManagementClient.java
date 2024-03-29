/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelreservationsystemclient;

import ejb.session.stateful.AllocationSessionBeanRemote;
import ejb.session.stateful.HotelReservationSessionBeanRemote;
import ejb.session.stateless.CustomersEntitySessionBeanRemote;
import ejb.session.stateless.EmployeesEntitySessionBeanRemote;
import ejb.session.stateless.HotelRoomsEntitySessionBeanRemote;
import ejb.session.stateless.RatesEntitySessionBeanRemote;
import ejb.session.stateless.ReservationsEntitySessionBeanRemote;
import ejb.session.stateless.RoomTypesEntitySessionBeanRemote;
import entity.Employees;
import java.util.InputMismatchException;
import java.util.Scanner;
import util.exception.InvalidAccessRightException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author tshuenhau
 */
public class ManagementClient {
    
    private ReservationsEntitySessionBeanRemote reservationsEntitySessionBeanRemote;

    private CustomersEntitySessionBeanRemote customersEntitySessionBeanRemote;

    private RatesEntitySessionBeanRemote ratesEntitySessionBeanRemote;

    private HotelRoomsEntitySessionBeanRemote hotelRoomsEntitySessionBeanRemote;

    private EmployeesEntitySessionBeanRemote employeesEntitySessionBeanRemote;
    
    private RoomTypesEntitySessionBeanRemote roomTypesEntitySessionBeanRemote;
    
    private HotelReservationSessionBeanRemote hotelReservationSessionBeanRemote;
    
    private AllocationSessionBeanRemote allocationSessionBeanRemote;
    
    
    private SystemAdministrationModule systemAdministrationModule; 
    private OperationManagerModule operationManagerModule;
    private SalesManagerModule salesManagerModule;
    private GuestRelationOfficerModule guestRelationOfficerModule; 
    
    private Employees currentEmployee;
    
    
    public ManagementClient() {
        this.currentEmployee = null;
    }

    public ManagementClient(HotelReservationSessionBeanRemote hotelReservationSessionBeanRemote, AllocationSessionBeanRemote allocationSessionBeanRemote, ReservationsEntitySessionBeanRemote reservationsEntitySessionBeanRemote, CustomersEntitySessionBeanRemote customersEntitySessionBeanRemote, RatesEntitySessionBeanRemote ratesEntitySessionBeanRemote, HotelRoomsEntitySessionBeanRemote hotelRoomsEntitySessionBeanRemote, EmployeesEntitySessionBeanRemote employeeEntitySessionBeanRemote, RoomTypesEntitySessionBeanRemote roomTypesEntitySessionBeanRemote) {
        this.hotelReservationSessionBeanRemote = hotelReservationSessionBeanRemote;
        this.allocationSessionBeanRemote = allocationSessionBeanRemote;
        this.reservationsEntitySessionBeanRemote = reservationsEntitySessionBeanRemote;
        this.customersEntitySessionBeanRemote = customersEntitySessionBeanRemote;
        this.ratesEntitySessionBeanRemote = ratesEntitySessionBeanRemote;
        this.hotelRoomsEntitySessionBeanRemote = hotelRoomsEntitySessionBeanRemote;
        this.employeesEntitySessionBeanRemote = employeeEntitySessionBeanRemote;
        this.roomTypesEntitySessionBeanRemote = roomTypesEntitySessionBeanRemote;
    }
    
    public void runApp(){
        Scanner scanner = new Scanner(System.in);

        Integer response = 0;
        
        try {
            while (true) {
                System.out.println("*** Welcome to Holiday Reservation System Management Client ***\n");

                if (currentEmployee != null) {
                    System.out.println("You are currently logged in as " + currentEmployee.getUsername() + "\n");
                } else {
                    System.out.println("1: Login");
                }

                System.out.println("2: Exit\n");

                response = 0;

                while (response < 1 || response > 2) {
                    System.out.print("> ");
                    response = scanner.nextInt();
                    if (response == 1) {
                        if (currentEmployee == null) {
                            try {
                                doLogin();
                                System.out.println("Login successful as " + currentEmployee.getUsername() + "!\n");
                                if (currentEmployee.getEmployeeType().equals("System Administrator")) {
                                    systemAdministrationModule = new SystemAdministrationModule(employeesEntitySessionBeanRemote, currentEmployee, customersEntitySessionBeanRemote, allocationSessionBeanRemote);
                                    menuMainSystemAdmin();
                                } else if (currentEmployee.getEmployeeType().equals("Operation Manager")) {
                                    operationManagerModule = new OperationManagerModule(employeesEntitySessionBeanRemote, currentEmployee, roomTypesEntitySessionBeanRemote, hotelRoomsEntitySessionBeanRemote);
                                    menuMainOperationManager();
                                } else if (currentEmployee.getEmployeeType().equals("Sales Manager")) {
                                    salesManagerModule = new SalesManagerModule(employeesEntitySessionBeanRemote, currentEmployee, roomTypesEntitySessionBeanRemote, hotelRoomsEntitySessionBeanRemote, ratesEntitySessionBeanRemote);
                                    menuMainSalesManager();
                                } else if (currentEmployee.getEmployeeType().equals("Guest Relation Officer")) {
                                    guestRelationOfficerModule = new GuestRelationOfficerModule(allocationSessionBeanRemote, employeesEntitySessionBeanRemote, currentEmployee, roomTypesEntitySessionBeanRemote, hotelRoomsEntitySessionBeanRemote, ratesEntitySessionBeanRemote, hotelReservationSessionBeanRemote, reservationsEntitySessionBeanRemote, customersEntitySessionBeanRemote);
                                    menuMainGuestRelationOfficer();
                                }
                            } catch (InvalidLoginCredentialException ex) {
                                System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                            }
                        } else {
                            System.out.println("You are already login as " + currentEmployee.getUsername() + "\n");
                        }
                    }
                }

                if (response == 2) {
                    System.out.println("Exited Reservation Client\n");
                    break;
                }
            }
        } catch (InputMismatchException ex) {
                scanner.nextLine();
                System.err.println("Input Mismatch.");
        }
    }
    
        private void doLogin() throws InvalidLoginCredentialException {
            Scanner scanner = new Scanner(System.in);
            String username = "";
            String password = "";

            System.out.println("*** Holiday Reservation System :: Login ***\n");
            System.out.print("Enter username> ");
            username = scanner.nextLine().trim();
            System.out.print("Enter password> ");
            password = scanner.nextLine().trim();

            if(username.length() > 0 && password.length() > 0){
                currentEmployee = employeesEntitySessionBeanRemote.login(username, password);
            }
            else {
                throw new InvalidLoginCredentialException("Missing login credential!");
            }
        }
        
        private void menuMainSystemAdmin(){
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        try {
            while(true){
                System.out.println("*** Hotel Reservation System Management Client ***\n");
                System.out.println("You are login as " + currentEmployee.getUsername() + " with " + currentEmployee.getEmployeeType() + " rights\n");
                System.out.println("1: System Administration");
                System.out.println("2: Logout\n");
                response = 0;

                while(response < 1 || response > 2){
                    System.out.print("> ");

                    response = scanner.nextInt();

                    if(response == 1){
                        try {
                            systemAdministrationModule.menuSystemAdministration();
                        }
                        catch (InvalidAccessRightException ex){
                            System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                        }
                    }
                    else if (response == 2){
                        currentEmployee = null;
                        break;
                    }
                    else {
                        System.out.println("Invalid option, please try again!\n");                
                    }
                }

                if(response == 2){
                    break;
                }
            }
        } catch (InputMismatchException ex) {
                scanner.nextLine();
                System.err.println("Input Mismatch.");
        }
    }
        
    private void menuMainOperationManager(){
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        try {
            while(true){
                System.out.println("*** Hotel Reservation System Management Client ***\n");
                System.out.println("You are login as " + currentEmployee.getUsername() + " with " + currentEmployee.getEmployeeType() + " rights\n");
                System.out.println("1: Operation Management");
                System.out.println("2: Logout\n");
                response = 0;

                while(response < 1 || response > 2){
                    System.out.print("> ");

                    response = scanner.nextInt();

                    if(response == 1){
                        try {
                            operationManagerModule.menuOperationManager();
                        }
                        catch (InvalidAccessRightException ex){
                            System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                        }
                    }
                    else if (response == 2){
                        currentEmployee = null;
                        break;
                    }
                    else {
                        System.out.println("Invalid option, please try again!\n");                
                    }
                }

                if(response == 2){
                    break;
                }
            }
        } catch (InputMismatchException ex) {
                scanner.nextLine();
                System.err.println("Input Mismatch.");
        }
    }
    
    
    private void menuMainSalesManager(){
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        try {
            while(true){
                System.out.println("*** Hotel Reservation System Management Client ***\n");
                System.out.println("You are login as " + currentEmployee.getUsername() + " with " + currentEmployee.getEmployeeType() + " rights\n");
                System.out.println("1: Sales Manager");
                System.out.println("2: Logout\n");
                response = 0;

                while(response < 1 || response > 2){
                    System.out.print("> ");

                    response = scanner.nextInt();

                    if(response == 1){
                        try {
                            salesManagerModule.menuSalesManager();
                        }
                        catch (InvalidAccessRightException ex){
                            System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                        }
                    }
                    else if (response == 2){
                        currentEmployee = null;
                        break;
                    }
                    else {
                        System.out.println("Invalid option, please try again!\n");                
                    }
                }

                if(response == 2){
                    break;
                }
            }
        } catch (InputMismatchException ex) {
                scanner.nextLine();
                System.err.println("Input Mismatch.");
        }
    }
    
    private void menuMainGuestRelationOfficer(){
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        try {
            while(true){
                System.out.println("*** Hotel Reservation System Management Client ***\n");
                System.out.println("You are login as " + currentEmployee.getUsername() + " with " + currentEmployee.getEmployeeType() + " rights\n");
                System.out.println("1: Guest Relation Officer");
                System.out.println("2: Logout\n");
                response = 0;

                while(response < 1 || response > 2){
                    System.out.print("> ");

                    response = scanner.nextInt();

                    if(response == 1){
                        try {
                            guestRelationOfficerModule.menuGuestRelationOfficer();
                        }
                        catch (InvalidAccessRightException ex){
                            System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                        }
                    }
                    else if (response == 2){
                        currentEmployee = null;
                        break;
                    }
                    else {
                        System.out.println("Invalid option, please try again!\n");                
                    }
                }

                if(response == 2){
                    break;
                }
            }
        } catch (InputMismatchException ex) {
                scanner.nextLine();
                System.err.println("Input Mismatch.");
        }
    }
}
