package hotelreservationsystemclient;

import entity.Employees;
import java.util.Scanner;
import ejb.session.stateless.EmployeesEntitySessionBeanRemote;
import java.util.List;
import util.exception.InvalidAccessRightException;



public class OperationManagerModule {
    private EmployeesEntitySessionBeanRemote employeesEntitySessionBeanRemote;
    
    private Employees currentEmployee;

    
    public OperationManagerModule(){
    }

    public OperationManagerModule(EmployeesEntitySessionBeanRemote employeesEntitySessionBeanRemote, Employees currentEmployee) {
        this.employeesEntitySessionBeanRemote = employeesEntitySessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }
    
    
    
    public void menuOperationManager() throws InvalidAccessRightException {
        if(!currentEmployee.getEmployeeType().equals("Operation Manager")){
            throw new InvalidAccessRightException("You don't have Operation Manager rights to access the system administration module.");
        }
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true){
            System.out.println("*** Hotel Reservation System Management Client :: Operation Manager ***\n");
            System.out.println("1: Create New Room Type");
            System.out.println("2: Update New Room Type");
            System.out.println("3: Delete Room Type");
            System.out.println("4: View All Room Types");
            System.out.println("-----------------------");
            System.out.println("5: Back\n");
            response = 0;
            
            while(response < 1 || response > 5){
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1){
                    //doCreateNewEmployee();
                }
                else if(response == 2){
                    //doViewAllEmployees();
                }
                else if(response == 3){
                    //create partner
                }
                else if(response == 4){
                    //view all partners
                }
                else if (response == 5){
                    break;
                }
                else {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 5){
                break;
            }
        }
    }
    
    
    /*
    private void doCreateNewEmployee(){
        Scanner scanner = new Scanner(System.in);
        Employees newEmployee = new Employees();
        
        System.out.println("*** Hotel Reservation System Management Client :: System Administration :: Create New Employee ***\n");
        
        while(true){
            System.out.print("Select Employee Type (1: System Administrator, 2: Operation Manager, 3: Sales Manager, 4: Guest Relation Officer)> ");
            Integer employeeTypeChosen = scanner.nextInt();
            
            if(employeeTypeChosen == 1){
                newEmployee.setEmployeeType("System Administrator");
                break;
            }
            else if (employeeTypeChosen == 2){
                newEmployee.setEmployeeType("Operation Manager");
                break;
            }
            else if (employeeTypeChosen == 3){
                newEmployee.setEmployeeType("Sales Manager");
                break;
            }
            else if (employeeTypeChosen == 4){
                newEmployee.setEmployeeType("Guest Relation Officer");
                break;
            }
            else{
                System.out.println("Invalid option, please try again!\n");
            }
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
        System.out.printf("%s%30s%20s\n", "Username", "Employee Type", "Password");

        for (Employees employee : employees) {
            System.out.printf("%s%30s%20s\n", employee.getUsername(), employee.getEmployeeType(), employee.getPassword());
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
*/
}