package hotelreservationsystemclient;

import ejb.session.stateful.AllocationSessionBeanRemote;
import entity.Employees;
import java.util.Scanner;
import ejb.session.stateless.EmployeesEntitySessionBeanRemote;
import ejb.session.stateless.CustomersEntitySessionBeanRemote;
import entity.Customers;
import static hotelreservationsystemclient.SalesManagerModule.inputDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import util.exception.AllocationException;
import util.exception.InvalidAccessRightException;
import util.exception.UserAlreadyExistException;



public class SystemAdministrationModule {
    private EmployeesEntitySessionBeanRemote employeesEntitySessionBeanRemote;
    private CustomersEntitySessionBeanRemote customersEntitySessionBeanRemote;
    private AllocationSessionBeanRemote allocationSessionBeanRemote;
    
    private Employees currentEmployee;

    
    public SystemAdministrationModule(){
    }

    public SystemAdministrationModule(EmployeesEntitySessionBeanRemote employeesEntitySessionBeanRemote, Employees currentEmployee, CustomersEntitySessionBeanRemote customersEntitySessionBeanRemote, AllocationSessionBeanRemote allocationSessionBeanRemote) {
        this.employeesEntitySessionBeanRemote = employeesEntitySessionBeanRemote;
        this.customersEntitySessionBeanRemote = customersEntitySessionBeanRemote;
        this.allocationSessionBeanRemote = allocationSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }
    
    
    
    public void menuSystemAdministration() throws InvalidAccessRightException {
        if(!currentEmployee.getEmployeeType().equals("System Administrator")){
            throw new InvalidAccessRightException("You don't have System Administrator rights to access the system administration module.");
        }
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        try {
            while(true){
                System.out.println("*** Hotel Reservation System Management Client :: System Administration ***\n");
                System.out.println("1: Create New Employee");
                System.out.println("2: View All Employees");
                System.out.println("-----------------------");
                System.out.println("3: Create New Partner");
                System.out.println("4: View All Partners");
                System.out.println("-----------------------");
                System.out.println("5: Allocate Rooms");
                System.out.println("6: Back\n");
                response = 0;

                while(response < 1 || response > 6){
                    System.out.print("> ");

                    response = scanner.nextInt();

                    if(response == 1){
                        try {
                            doCreateNewEmployee();
                        }
                        catch(UserAlreadyExistException ex){
                            System.out.println("User Already Exists!\n");
                        }
                    }
                    else if(response == 2){
                        doViewAllEmployees();
                    }
                    else if(response == 3){
                        try {
                            doCreateNewPartner();
                        }
                        catch(UserAlreadyExistException ex){
                            System.out.println("User Already Exists!\n");
                        }
                    }
                    else if(response == 4){
                        doViewAllPartners();
                    }
                    else if (response == 5){
                        doAllocateRoom();
                    }
                    else if (response == 6){
                        break;
                    }
                    else {
                        System.out.println("Invalid option, please try again!\n");                
                    }
                }

                if(response == 6){
                    break;
                }
            }
        } catch (InputMismatchException ex) {
                scanner.nextLine();
                System.err.println("Input Mismatch.");
        }
    }
    
    
    
    private void doCreateNewEmployee() throws UserAlreadyExistException {
        Scanner scanner = new Scanner(System.in);
        Employees newEmployee = new Employees();
        
        System.out.println("*** Hotel Reservation System Management Client :: System Administration :: Create New Employee ***\n");
        
        try {
            while (true) {
                System.out.print("Select Employee Type (1: System Administrator, 2: Operation Manager, 3: Sales Manager, 4: Guest Relation Officer)> ");
                Integer employeeTypeChosen = scanner.nextInt();

                if (employeeTypeChosen == 1) {
                    newEmployee.setEmployeeType("System Administrator");
                    break;
                } else if (employeeTypeChosen == 2) {
                    newEmployee.setEmployeeType("Operation Manager");
                    break;
                } else if (employeeTypeChosen == 3) {
                    newEmployee.setEmployeeType("Sales Manager");
                    break;
                } else if (employeeTypeChosen == 4) {
                    newEmployee.setEmployeeType("Guest Relation Officer");
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
        } catch (InputMismatchException ex) {
                scanner.nextLine();
                System.err.println("Input Mismatch.");
        }
        
        scanner.nextLine();
        System.out.print("Enter Username> ");
        newEmployee.setUsername(scanner.nextLine().trim());
        System.out.print("Enter Password> ");
        newEmployee.setPassword(scanner.nextLine().trim());
        
        String newEmployeeUsername = employeesEntitySessionBeanRemote.createNewEmployee(newEmployee);
        System.out.println("New employee created successfully!: " + newEmployeeUsername + "\n");
    }
   
    private void doViewAllEmployees(){
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Hotel Reservation Management Client System :: System Administration :: View All Employees ***\n");
        
        List<Employees> employees = employeesEntitySessionBeanRemote.retrieveAllEmployees();
        System.out.printf("%s%40s%20s\n", "Username", "Employee Type", "Password");

        for (Employees employee : employees) {
            System.out.printf("%s%40s%20s\n", employee.getUsername(), employee.getEmployeeType(), employee.getPassword());
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    
    private void doCreateNewPartner() throws UserAlreadyExistException{
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Hotel Reservation System Management Client :: System Administration :: Create New Partner ***\n");
        
        System.out.print("Enter Name> ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Passport Number> ");
        Long passportNum = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Enter Password> ");
        String password = scanner.nextLine().trim();
        
        Customers newPartner = customersEntitySessionBeanRemote.createNewCustomer(new Customers(passportNum, password, name, true));
        System.out.println("New partner created successfully!: " + newPartner.getPassportNum() + "\n");
    }
    
    
    private void doViewAllPartners(){
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Hotel Reservation Management Client System :: System Administration :: View All Partners ***\n");
        
        List<Customers> partners = customersEntitySessionBeanRemote.retrieveAllPartners();
        System.out.printf("%s%30s%30s\n", "Name", "Passport Number", "Password");

        for (Customers partner : partners) {
            System.out.printf("%s%30s%30s\n", partner.getName(), partner.getPassportNum(), partner.getPassword());
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    private void doAllocateRoom(){
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        Date date;
        
        System.out.println("*** Hotel Reservation Management Client System :: Operation Manager :: Allocate Room ***\n");
        
        
        try {
            System.out.print("Enter date (dd/mm/yyyy)> ");
            date = inputDateFormat.parse(scanner.nextLine().trim());
            allocationSessionBeanRemote.allocateRooms(date);
            
            System.out.println("------------------------");
            System.out.println("1: View Room Allocation Exception Report");
            System.out.println("2: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();

            if(response == 1) {
                doViewAllocationExceptionReport(date);
            }
        }
        catch(ParseException ex) {
            System.out.println("An error has occurred while allocation rooms: " + ex.getMessage() + "\n");
        }
    }
    
    
    private void doViewAllocationExceptionReport(Date date){
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Hotel Reservation Management Client System :: Operation Manager :: View Room Allocation Exception Report ***\n");
        
        List<AllocationException> allocationExceptions = allocationSessionBeanRemote.viewAllocationException(date);
        System.out.printf("%s%30s\n", "Room Type", "Exception Type");

        for (AllocationException allocationException : allocationExceptions) {
            System.out.printf("%s%30s\n", allocationException.getRoomType().getRoomTypeName(), allocationException.getExceptionType());
        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
}