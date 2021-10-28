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
            System.out.println("*** Welcome to Holiday Management Client ***\n");
                        
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
            String email = "";
            String password = "";

            System.out.println("*** Holiday Reservation System :: Login ***\n");
            System.out.print("Enter email> ");
            email = scanner.nextLine().trim();
            System.out.print("Enter password> ");
            password = scanner.nextLine().trim();

            if(email.length() > 0 && password.length() > 0)
            {
                currentEmployee = employeesEntitySessionBeanRemote.login(email, password);
            }
            else
            {
                throw new InvalidLoginCredentialException("Missing login credential!");
            }
    }
}
