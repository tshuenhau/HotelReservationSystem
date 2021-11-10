package hotelreservationsystemclient;

import entity.Employees;
import java.util.Scanner;
import ejb.session.stateless.EmployeesEntitySessionBeanRemote;
import entity.RoomTypes;
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
            System.out.println("2: Update Room Type");
            System.out.println("3: Delete Room Type");
            System.out.println("4: View All Room Types");
            System.out.println("-----------------------");
            System.out.println("5: Create New Room");
            System.out.println("6: Update Room");
            System.out.println("7: Delete Room");
            System.out.println("8: View All Rooms");
            System.out.println("-----------------------");
            System.out.println("9: Create New Room Rate");
            System.out.println("10: Update Room Rate");
            System.out.println("11: Delete Room Rate");
            System.out.println("12: View All Room Rate Details");
            System.out.println("-----------------------");
            System.out.println("13: Back\n");
            response = 0;
            
            while(response < 1 || response > 13){
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1){
                    //doCreateNewRoomType();
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
            
            if(response == 13){
                break;
            }
        }
    }
    
    
    /*
    private void doCreateNewRoomType(){
        Scanner scanner = new Scanner(System.in);
        RoomTypes newRoomType = new RoomTypes();
        
        System.out.println("*** Hotel Reservation System Management Client :: Operation Manager :: Create New Room Type ***\n");
        
        System.out.print("Enter Name of Room> ");
        newRoomType.setRoomTypeName(scanner.nextLine().trim());
        System.out.print("Enter NextHigherRoomType> ");
        String nextHigherRoomType = scanner.nextLine().trim();
        newRoomType.setNextHigherRoomType(new RoomTypes);
        
        String newEmployeeUsername = employeesEntitySessionBeanRemote.createNewRoomType(newEmployee);
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