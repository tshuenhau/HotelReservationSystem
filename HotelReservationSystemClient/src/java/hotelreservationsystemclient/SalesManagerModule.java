package hotelreservationsystemclient;

import entity.Employees;
import java.util.Scanner;
import ejb.session.stateless.EmployeesEntitySessionBeanRemote;
import ejb.session.stateless.HotelRoomsEntitySessionBeanRemote;
import ejb.session.stateless.RatesEntitySessionBeanRemote;
import ejb.session.stateless.RoomTypesEntitySessionBeanRemote;
import entity.Rates;
import entity.RoomTypes;
import java.util.List;
import util.exception.InvalidAccessRightException;
import util.exception.RoomTypeNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import util.exception.RateAlreadyExistException;
import util.exception.RatesNotFoundException;



public class SalesManagerModule {
    private EmployeesEntitySessionBeanRemote employeesEntitySessionBeanRemote;
    private RoomTypesEntitySessionBeanRemote roomTypesEntitySessionBeanRemote;
    private HotelRoomsEntitySessionBeanRemote hotelRoomsEntitySessionBeanRemote;
    private RatesEntitySessionBeanRemote ratesEntitySessionBeanRemote;
    
    private Employees currentEmployee;
    
    static SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
    static SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");//"dd/MM/yyyy hh:mm a"

    
    public SalesManagerModule(){
    }

    public SalesManagerModule(EmployeesEntitySessionBeanRemote employeesEntitySessionBeanRemote, Employees currentEmployee, RoomTypesEntitySessionBeanRemote roomTypesEntitySessionBeanRemote, HotelRoomsEntitySessionBeanRemote hotelRoomsEntitySessionBeanRemote, RatesEntitySessionBeanRemote ratesEntitySessionBeanRemote) {
        this.employeesEntitySessionBeanRemote = employeesEntitySessionBeanRemote;
        this.roomTypesEntitySessionBeanRemote = roomTypesEntitySessionBeanRemote;
        this.hotelRoomsEntitySessionBeanRemote = hotelRoomsEntitySessionBeanRemote;
        this.ratesEntitySessionBeanRemote = ratesEntitySessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }
    
    
    
    public void menuSalesManager() throws InvalidAccessRightException {
        if(!currentEmployee.getEmployeeType().equals("Sales Manager")){
            throw new InvalidAccessRightException("You don't have Sales Manager rights to access the sales manager module.");
        }
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        try {
            while(true){
                System.out.println("*** Hotel Reservation System Management Client :: Sales Manager ***\n");
                System.out.println("1: Create New Room Rate");
                System.out.println("2: View Room Rate Details");
                System.out.println("3: View All Room Rates");
                System.out.println("-----------------------");
                System.out.println("4: Back\n");
                response = 0;

                while(response < 1 || response > 4){
                    System.out.print("> ");

                    response = scanner.nextInt();

                    if(response == 1){
                        doCreateNewRoomRate();
                    }
                    else if(response == 2){
                        doViewRoomRateDetails();
                    }
                    else if(response == 3){
                        doViewAllRoomRates();
                    }
                    else if (response == 4){
                        break;
                    }
                    else {
                        System.out.println("Invalid option, please try again!\n");                
                    }
                }

                if(response == 4){
                    break;
                }
            }
        } catch (InputMismatchException ex) {
                scanner.nextLine();
                System.err.println("Input Mismatch.");
        }
    }
    
    
    
    private void doCreateNewRoomRate(){
        Scanner scanner = new Scanner(System.in);
        
        Rates newRate = new Rates();
        String newRateType = "";
        
        System.out.println("*** Hotel Reservation System Management Client :: Sales Manager :: Create New Room Rate ***\n");
        
        try {
            while (true) {
                System.out.print("Enter New Rate Type Name (Published / Normal / Peak / Promo): ");
                newRateType = scanner.nextLine().trim();
                if (newRateType.equals("Published")) {
                    newRate.setRateType(newRateType);
                    break;
                } else if (newRateType.equals("Normal")) {
                    newRate.setRateType(newRateType);
                    break;
                } else if (newRateType.equals("Peak")) {
                    newRate.setRateType(newRateType);
                    try {
                        System.out.print("Enter Peak rate type validity period start date (DD/MM/YYYY): ");
                        newRate.setStartDate(inputDateFormat.parse(scanner.nextLine().trim()));
                    } catch (ParseException ex) {
                        System.out.println("Parse Exception");
                    }

                    try {
                        System.out.print("Enter Peak rate type validity period end date (DD/MM/YYYY): ");
                        newRate.setEndDate(inputDateFormat.parse(scanner.nextLine().trim()));
                    } catch (ParseException ex) {
                        System.out.println("Parse Exception");
                    }
                    break;
                } else if (newRateType.equals("Promo")) {
                    newRate.setRateType(newRateType);

                    try {
                        System.out.print("Enter Peak rate type validity period start date (DD/MM/YYYY): ");
                        newRate.setStartDate(inputDateFormat.parse(scanner.nextLine().trim()));
                    } catch (ParseException ex) {
                        System.out.println("Parse Exception");
                    }

                    try {
                        System.out.print("Enter Peak rate type validity period end date (DD/MM/YYYY): ");
                        newRate.setEndDate(inputDateFormat.parse(scanner.nextLine().trim()));
                    } catch (ParseException ex) {
                        System.out.println("Parse Exception");
                    }
                    break;
                } else {
                    System.out.println("Invalid option, please ensure it is one of the four rate types!\n");
                }
            }
        } catch (InputMismatchException ex) {
                scanner.nextLine();
                System.err.println("Input Mismatch.");
        }
        
        System.out.print("Enter New Rate Room Type: ");
        String newRatesRoomType = scanner.nextLine().trim();
        try {
            RoomTypes roomType = roomTypesEntitySessionBeanRemote.retrievesRoomTypeByRoomTypeName(newRatesRoomType);
            newRate.setRoomType(roomType);
        } catch(RoomTypeNotFoundException ex) {
            System.out.println("An error has occurred while retrieving room type: " + ex.getMessage() + "\n");
        }
        
        System.out.print("Enter New Rate Pricing per night: ");
        Integer newRatesPricing = scanner.nextInt();
        newRate.setPrice(newRatesPricing);
        scanner.nextLine();
        
        try {
            Rates createdRate = ratesEntitySessionBeanRemote.createNewRate(newRate);
            System.out.println("New Room Rate created successfully: " + createdRate.getRoomType().getRoomTypeName() + " " + createdRate.getRateType() + "!\n");
        } catch (RateAlreadyExistException ex) {
            System.out.println("An error has occurred while creating room rate: " + ex.getMessage() + "\n");
        }
        
    }
    
    
    private void doViewRoomRateDetails(){
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** Hotel Reservation Management Client System :: Sales Manager :: View Room Rate Details ***\n");
        System.out.print("Select Room Rate ID: > ");
        Long rateId = scanner.nextLong();
        scanner.nextLine();
        
        try {
            Rates rateEntity = ratesEntitySessionBeanRemote.retrievesRatesByRateID(rateId);
            System.out.printf("%s%30s%40s%20s%20s%20s\n", "Rate ID", "Rate Type", "Room Type", "Price", "Start Date", "End Date");
            System.out.printf("%s%30s%40s%20s%20s%20s\n", rateEntity.getRateID(), rateEntity.getRateType(), rateEntity.getRoomType().getRoomTypeName(), rateEntity.getPrice(), outputDateFormat.format(rateEntity.getStartDate()), outputDateFormat.format(rateEntity.getEndDate()));
            
            System.out.println("------------------------");
            System.out.println("1: Update Room Type");
            System.out.println("2: Delete Room Type");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();

            if(response == 1) {
                //doUpdateRoomType(rateEntity);
            }
            else if(response == 2)
            {
                //doDeleteRoomType(rateEntity);
            }
        }
        catch(RatesNotFoundException ex) {
            System.out.println("An error has occurred while retrieving rates: " + ex.getMessage() + "\n");
        }
    }
    
    /*
    private void doUpdateRate(Rates rateEntity) {
        Scanner scanner = new Scanner(System.in);        
        String input;
        
        System.out.println("*** Hotel Reservation Management Client System :: Operation Manager :: View Room Type Details :: Update Room Type ***\n");
        System.out.println("Enter new name to be updated: ");
        input = scanner.nextLine().trim();
        if(input.length() > 0){
            roomTypeEntity.setRoomTypeName(input);
            System.out.print("Room Type Name updated successfully! ");
        }
    }
    
    
    private void doDeleteRate(Rates rateEntity){
        Scanner scanner = new Scanner(System.in);        
        String input;
        
        System.out.println("*** Hotel Reservation Management Client System :: Operation Manager :: View Room Type Details :: Delete Room Type ***\n");
        System.out.printf("Confirm Delete Room Type %s (Enter 'Y' to Delete)> ", roomTypeEntity.getRoomTypeName());
        input = scanner.nextLine().trim();
        
        if(input.equals("Y")) {
            try {
                roomTypesEntitySessionBeanRemote.deleteRoomType(roomTypeEntity.getRoomTypeName());
                System.out.println("Staff deleted successfully!\n");
            }
            catch (RoomTypeNotFoundException | DeleteRoomTypeException ex) {
                System.out.println("An error has occurred while deleting the staff: " + ex.getMessage() + "\n");
            }
        }
        else {
            System.out.println("Room Type NOT deleted!\n");
        }
    }
*/
    
    
    private void doViewAllRoomRates(){
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Hotel Reservation Management Client System :: Sales Manager :: View All Room Rates ***\n");
        
        List<Rates> roomRates = ratesEntitySessionBeanRemote.retrieveAllRates();
        System.out.printf("%s%30s%30s%20s%20s%20s\n", "Rate ID", "Rate Type", "Room Type", "Price", "Start Date", "End Date");

        for (Rates rate : roomRates) {
            System.out.printf("%s%30s%30s%20s%20s%20s\n", rate.getRateID(), rate.getRateType(), rate.getRoomType().getRoomTypeName(), rate.getPrice(), rate.getStartDate(), rate.getEndDate());
        }
        
        System.out.println("Press any key to continue...> ");
        scanner.nextLine();
    }
}