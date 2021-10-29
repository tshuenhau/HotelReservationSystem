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
import entity.Employees;
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
    
    private SystemAdministrationModule systemAdministrationModule; 
    
    private Employees currentEmployee;
    
    
    public ManagementClient() {
        this.currentEmployee = null;
    }

    public ManagementClient(ReservationsEntitySessionBeanRemote reservationsEntitySessionBeanRemote, CustomersEntitySessionBeanRemote customersEntitySessionBeanRemote, RatesEntitySessionBeanRemote ratesEntitySessionBeanRemote, HotelRoomsEntitySessionBeanRemote hotelRoomsEntitySessionBeanRemote, EmployeesEntitySessionBeanRemote employeeEntitySessionBeanRemote) {
        this.reservationsEntitySessionBeanRemote = reservationsEntitySessionBeanRemote;
        this.customersEntitySessionBeanRemote = customersEntitySessionBeanRemote;
        this.ratesEntitySessionBeanRemote = ratesEntitySessionBeanRemote;
        this.hotelRoomsEntitySessionBeanRemote = hotelRoomsEntitySessionBeanRemote;
        this.employeesEntitySessionBeanRemote = employeeEntitySessionBeanRemote;
    }
    
    public void runApp(){
        Scanner scanner = new Scanner(System.in);

        Integer response = 0;

        while(true){
            System.out.println("*** Welcome to Holiday Reservation System Management Client ***\n");
                        
            if(currentEmployee != null){
                System.out.println("You are currently logged in as " + currentEmployee.getUsername() + "\n");
            }
            else {            
                System.out.println("1: Login");
            }
            
            System.out.println("2: Exit\n");
            
            response = 0;

            while(response < 1 || response > 2){                
                System.out.print("> ");
                response = scanner.nextInt();
                if(response == 1 ){
                    if(currentEmployee == null){
                        try {
                            doLogin();
                            System.out.println("Login successful as " + currentEmployee.getUsername() + "!\n");
                            systemAdministrationModule = new SystemAdministrationModule(employeesEntitySessionBeanRemote, currentEmployee);
                            menuMain();
                        }
                        catch(InvalidLoginCredentialException ex){
                            System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                        }
                    }
                    else {
                        System.out.println("You are already login as " + currentEmployee.getUsername() + "\n");
                    }
                }
                else if(response == 2 ){
                    //DO REGISTER STUFF
                }
                else if (response == 3){
                    //
                }
            }
            
            if(response == 2){
                System.out.println("Exited Reservation Client\n");
                break;
            }
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
        
        private void menuMain(){
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
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
    }
}
