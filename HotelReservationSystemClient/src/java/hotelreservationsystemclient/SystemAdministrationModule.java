package hotelreservationsystemclient;

import entity.Employees;
import java.util.Scanner;
import ejb.session.stateless.EmployeesEntitySessionBeanRemote;
import util.exception.InvalidAccessRightException;



public class SystemAdministrationModule {
    private EmployeesEntitySessionBeanRemote employeesEntitySessionBeanRemote;
    
    private Employees currentEmployee;

    
    public SystemAdministrationModule(){
    }

    public SystemAdministrationModule(EmployeesEntitySessionBeanRemote employeesEntitySessionBeanRemote, Employees currentEmployee) {
        this.employeesEntitySessionBeanRemote = employeesEntitySessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }
    
    
    
    public void menuSystemAdministration() throws InvalidAccessRightException {
        if(!currentEmployee.getEmployeeType().equals("System Administrator")){
            throw new InvalidAccessRightException("You don't have System Administrator rights to access the system administration module.");
        }
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true){
            System.out.println("*** Hotel Reservation System Management Client :: System Administration ***\n");
            System.out.println("1: Create New Employee");
            System.out.println("2: View All Staffs");
            System.out.println("-----------------------");
            System.out.println("3: Create New Partner");
            System.out.println("4: View All Partners");
            System.out.println("-----------------------");
            System.out.println("5: Back\n");
            response = 0;
            
            while(response < 1 || response > 5){
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1){
                    doCreateNewStaff();
                }
                else if(response == 2){
                    //doViewStaffDetails();
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
    
    
    
    private void doCreateNewStaff(){
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
    
    
    /*
    private void doViewStaffDetails()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** POS System :: System Administration :: View Staff Details ***\n");
        System.out.print("Enter Staff ID> ");
        Long staffId = scanner.nextLong();
        
        try
        {
            StaffEntity staffEntity = staffEntitySessionBeanRemote.retrieveStaffByStaffId(staffId);
            System.out.printf("%8s%20s%20s%15s%20s%20s\n", "Staff ID", "First Name", "Last Name", "Access Right", "Username", "Password");
            System.out.printf("%8s%20s%20s%15s%20s%20s\n", staffEntity.getStaffId().toString(), staffEntity.getFirstName(), staffEntity.getLastName(), staffEntity.getAccessRightEnum().toString(), staffEntity.getUsername(), staffEntity.getPassword());         
            System.out.println("------------------------");
            System.out.println("1: Update Staff");
            System.out.println("2: Delete Staff");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();

            if(response == 1)
            {
                doUpdateStaff(staffEntity);
            }
            else if(response == 2)
            {
                doDeleteStaff(staffEntity);
            }
        }
        catch(EntityManagerException | StaffNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving staff: " + ex.getMessage() + "\n");
        }
    }
    
    
    private void doUpdateStaff(StaffEntity staffEntity)
    {
        Scanner scanner = new Scanner(System.in);        
        String input;
        
        System.out.println("*** POS System :: System Administration :: View Staff Details :: Update Staff ***\n");
        System.out.print("Enter First Name (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            staffEntity.setFirstName(input);
        }
                
        System.out.print("Enter Last Name (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            staffEntity.setLastName(input);
        }
        
        while(true)
        {
            System.out.print("Select Access Right (0: No Change, 1: Cashier, 2: Manager)> ");
            Integer accessRightInt = scanner.nextInt();
            
            if(accessRightInt >= 1 && accessRightInt <= 2)
            {
                staffEntity.setAccessRightEnum(AccessRightEnum.values()[accessRightInt-1]);
                break;
            }
            else if (accessRightInt == 0)
            {
                break;
            }
            else
            {
                System.out.println("Invalid option, please try again!\n");
            }
        }
        
        try
        {
            staffEntitySessionBeanRemote.updateStaff(staffEntity);
            System.out.println("Staff updated successfully!\n");
        }
        catch (EntityManagerException ex) 
        {
            System.out.println("An error has occurred while updating the staff: " + ex.getMessage() + "\n");
        }
    }
    
    
    
    private void doDeleteStaff(StaffEntity staffEntity)
    {
        Scanner scanner = new Scanner(System.in);        
        String input;
        
        System.out.println("*** POS System :: System Administration :: View Staff Details :: Delete Staff ***\n");
        System.out.printf("Confirm Delete Staff %s %s (Staff ID: %d) (Enter 'Y' to Delete)> ", staffEntity.getFirstName(), staffEntity.getLastName(), staffEntity.getStaffId());
        input = scanner.nextLine().trim();
        
        if(input.equals("Y"))
        {
            try
            {
                staffEntitySessionBeanRemote.deleteStaff(staffEntity);
                System.out.println("Staff deleted successfully!\n");
            }
            catch (EntityManagerException ex) 
            {
                System.out.println("An error has occurred while deleting the staff: " + ex.getMessage() + "\n");
            }
        }
        else
        {
            System.out.println("Staff NOT deleted!\n");
        }
    }
    
    
    
    private void doViewAllStaffs()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** POS System :: System Administration :: View All Staffs ***\n");
        
        try
        {
            List<StaffEntity> staffEntities = staffEntitySessionBeanRemote.retrieveAllStaffs();
            System.out.printf("%8s%20s%20s%15s%20s%20s\n", "Staff ID", "First Name", "Last Name", "Access Right", "Username", "Password");
            
            for(StaffEntity staffEntity:staffEntities)
            {
                System.out.printf("%8s%20s%20s%15s%20s%20s\n", staffEntity.getStaffId().toString(), staffEntity.getFirstName(), staffEntity.getLastName(), staffEntity.getAccessRightEnum().toString(), staffEntity.getUsername(), staffEntity.getPassword());
            }
        }
        catch(EntityManagerException ex)
        {
            System.out.println("An error has occurred while retrieving all staffs: " + ex.getMessage() + "\n");
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    
    
    private void doCreateNewProduct()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** POS System :: System Administration :: Create New Product ***\n");
        System.out.println("Unsupported operation!\n");
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    
    
    private void doViewProductDetails()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** POS System :: System Administration :: View Product Details ***\n");
        System.out.println("Unsupported operation!\n");
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    
    
    private void doViewAllProducts()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** POS System :: System Administration :: View All Products ***\n");
        
        try
        {
            List<ProductEntity> productEntities = productEntitySessionBeanRemote.retrieveAllProducts();
            System.out.printf("%10s%20s%20s%20s%13s%20s\n", "SKU Code", "Name", "Description", "Quantity On Hand", "Unit Price", "Category");
            
            for(ProductEntity productEntity:productEntities)
            {
                System.out.printf("%10s%20s%20s%20d%13s%20s\n", productEntity.getSkuCode(), productEntity.getName(), productEntity.getDescription(), productEntity.getQuantityOnHand(), NumberFormat.getCurrencyInstance().format(productEntity.getUnitPrice()), productEntity.getCategory());
            }
        }
        catch(EntityManagerException ex)
        {
            System.out.println("An error has occurred while retrieving all products: " + ex.getMessage() + "\n");
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
*/
}