package hotelreservationsystemclient;

import ejb.session.stateful.HotelReservationSessionBeanRemote;
import entity.Employees;
import java.util.Scanner;
import ejb.session.stateless.EmployeesEntitySessionBeanRemote;
import ejb.session.stateless.HotelRoomsEntitySessionBeanRemote;
import ejb.session.stateless.RatesEntitySessionBeanRemote;
import ejb.session.stateless.RoomTypesEntitySessionBeanRemote;
import entity.Rates;
import entity.RoomTypes;
import java.text.SimpleDateFormat;
import java.util.List;
import util.exception.DeleteRoomTypeException;
import util.exception.InvalidAccessRightException;
import util.exception.RoomTypeNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;



public class GuestRelationOfficerModule {
    private EmployeesEntitySessionBeanRemote employeesEntitySessionBeanRemote;
    private RoomTypesEntitySessionBeanRemote roomTypesEntitySessionBeanRemote;
    private HotelRoomsEntitySessionBeanRemote hotelRoomsEntitySessionBeanRemote;
    private RatesEntitySessionBeanRemote ratesEntitySessionBeanRemote;
    private HotelReservationSessionBeanRemote hotelReservationSessionBeanRemote;
    
    private Employees currentEmployee;
    
    SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    
    public GuestRelationOfficerModule(){
    }

    public GuestRelationOfficerModule(EmployeesEntitySessionBeanRemote employeesEntitySessionBeanRemote, Employees currentEmployee, RoomTypesEntitySessionBeanRemote roomTypesEntitySessionBeanRemote, HotelRoomsEntitySessionBeanRemote hotelRoomsEntitySessionBeanRemote, RatesEntitySessionBeanRemote ratesEntitySessionBeanRemote, HotelReservationSessionBeanRemote hotelReservationSessionBeanRemote) {
        this.employeesEntitySessionBeanRemote = employeesEntitySessionBeanRemote;
        this.roomTypesEntitySessionBeanRemote = roomTypesEntitySessionBeanRemote;
        this.hotelRoomsEntitySessionBeanRemote = hotelRoomsEntitySessionBeanRemote;
        this.ratesEntitySessionBeanRemote = ratesEntitySessionBeanRemote;
        this.currentEmployee = currentEmployee;
        this.hotelReservationSessionBeanRemote = hotelReservationSessionBeanRemote;
    }
    
    
    
    public void menuGuestRelationOfficer() throws InvalidAccessRightException {
        if(!currentEmployee.getEmployeeType().equals("Guest Relation Officer")){
            throw new InvalidAccessRightException("You don't have Guest Relation Officer rights to access the guest relation officer module.");
        }
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true){
            System.out.println("*** Hotel Reservation System Management Client :: Guest Relation Officer ***\n");
            System.out.println("1: Walk-in Search Room");
            System.out.println("2: Check-in Guest");
            System.out.println("3: Check-out Guest");
            System.out.println("-----------------------");
            System.out.println("4: Back\n");
            response = 0;
            
            while(response < 1 || response > 4){
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1){
                    //doWalkInSearch();
                }
                else if(response == 2){
                    //doViewRoomRateDetails();
                }
                else if(response == 3){
                    //doViewAllRoomRates();
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
    }
    
    /*
    private void doWalkInSearch(){
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** Hotel Reservation Management Client System :: Guest Relation Officer :: Walk-in Search Room ***\n");
        try {
            System.out.print("Enter check-in date (DD/MM/YYYY): ");
            newRate.setStartDate(inputDateFormat.parse(scanner.nextLine().trim()));
            System.out.print("Enter check-out date (DD/MM/YYYY): ");
            newRate.setStartDate(inputDateFormat.parse(scanner.nextLine().trim()));
            hotelReservationSessionBeanRemote
        } catch (ParseException ex) {
            System.out.println("Parse Exception");
        }
    }
*/

}