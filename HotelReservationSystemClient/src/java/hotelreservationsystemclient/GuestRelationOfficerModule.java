package hotelreservationsystemclient;

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
    
    private Employees currentEmployee;
    
    SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    
    public GuestRelationOfficerModule(){
    }

    public GuestRelationOfficerModule(EmployeesEntitySessionBeanRemote employeesEntitySessionBeanRemote, Employees currentEmployee, RoomTypesEntitySessionBeanRemote roomTypesEntitySessionBeanRemote, HotelRoomsEntitySessionBeanRemote hotelRoomsEntitySessionBeanRemote, RatesEntitySessionBeanRemote ratesEntitySessionBeanRemote) {
        this.employeesEntitySessionBeanRemote = employeesEntitySessionBeanRemote;
        this.roomTypesEntitySessionBeanRemote = roomTypesEntitySessionBeanRemote;
        this.hotelRoomsEntitySessionBeanRemote = hotelRoomsEntitySessionBeanRemote;
        this.ratesEntitySessionBeanRemote = ratesEntitySessionBeanRemote;
        this.currentEmployee = currentEmployee;
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
        
        System.out.println("*** Hotel Reservation Management Client System :: Operation Manager :: View Room Rate Details ***\n");
        System.out.print("Select Room Rate ID: > ");
        String roomTypeNameChosen = scanner.nextLine().trim();
        
        try {
            RoomTypes roomTypeEntity = roomTypesEntitySessionBeanRemote.retrievesRoomTypeByRoomTypeName(roomTypeNameChosen);
            System.out.printf("%10s%20s%20s%15s%20s%40s%40s\n", "Room Type Name", "Next Higher Room Type", "Description", "Size", "Bed", "Capacity", "Amenities");
            if (roomTypeEntity.getNextHigherRoomType() == null) {
                System.out.printf("%10s%30s%20s%15s%20s%20s%20s\n", roomTypeEntity.getRoomTypeName(), "null", roomTypeEntity.getDescription(), roomTypeEntity.getSize(), roomTypeEntity.getBed(), roomTypeEntity.getCapacity(), roomTypeEntity.getAmenities());
            } else {
                System.out.printf("%10s%30s%20s%15s%20s%40s%40s\n", roomTypeEntity.getRoomTypeName(), roomTypeEntity.getNextHigherRoomType().getRoomTypeName(), roomTypeEntity.getDescription(), roomTypeEntity.getSize(), roomTypeEntity.getBed(), roomTypeEntity.getCapacity(), roomTypeEntity.getAmenities());         
            }
            System.out.println("------------------------");
            System.out.println("1: Walk-in Reserve");
            System.out.println("2: Back");
            System.out.print("> ");
            response = scanner.nextInt();

            if(response == 1) {
                doUpdateRoomType(roomTypeEntity);
            }
            else if(response == 2)
            {
                doDeleteRoomType(roomTypeEntity);
            }
        }
        catch(RoomTypeNotFoundException ex) {
            System.out.println("An error has occurred while retrieving staff: " + ex.getMessage() + "\n");
        }
    }
*/
}