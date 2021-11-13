package hotelreservationsystemclient;

import entity.Employees;
import java.util.Scanner;
import ejb.session.stateless.EmployeesEntitySessionBeanRemote;
import ejb.session.stateless.HotelRoomsEntitySessionBeanRemote;
import ejb.session.stateless.RoomTypesEntitySessionBeanRemote;
import entity.HotelRooms;
import entity.RoomTypes;
import java.text.SimpleDateFormat;
import java.util.List;
import util.exception.DeleteRoomException;
import util.exception.DeleteRoomTypeException;
import util.exception.InvalidAccessRightException;
import util.exception.RoomAlreadyExistException;
import util.exception.RoomNotFoundException;
import util.exception.RoomTypeAlreadyExistException;
import util.exception.RoomTypeNotFoundException;



public class OperationManagerModule {
    private EmployeesEntitySessionBeanRemote employeesEntitySessionBeanRemote;
    private RoomTypesEntitySessionBeanRemote roomTypesEntitySessionBeanRemote;
    private HotelRoomsEntitySessionBeanRemote hotelRoomsEntitySessionBeanRemote;
    
    private Employees currentEmployee;
    
    SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    public OperationManagerModule(){
    }

    public OperationManagerModule(EmployeesEntitySessionBeanRemote employeesEntitySessionBeanRemote, Employees currentEmployee, RoomTypesEntitySessionBeanRemote roomTypesEntitySessionBeanRemote, HotelRoomsEntitySessionBeanRemote hotelRoomsEntitySessionBeanRemote) {
        this.employeesEntitySessionBeanRemote = employeesEntitySessionBeanRemote;
        this.roomTypesEntitySessionBeanRemote = roomTypesEntitySessionBeanRemote;
        this.hotelRoomsEntitySessionBeanRemote = hotelRoomsEntitySessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }
    
    
    
    public void menuOperationManager() throws InvalidAccessRightException {
        if(!currentEmployee.getEmployeeType().equals("Operation Manager")){
            throw new InvalidAccessRightException("You don't have Operation Manager rights to access the operation manager module.");
        }
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true){
            System.out.println("*** Hotel Reservation System Management Client :: Operation Manager ***\n");
            System.out.println("1: Create New Room Type");
            System.out.println("2: View Room Type Details");
            System.out.println("3: View All Room Types");
            System.out.println("-----------------------");
            System.out.println("4: Create New Room");
            System.out.println("5: Update Room Status");
            System.out.println("6: Delete Room");
            System.out.println("7: View All Rooms");
            System.out.println("-----------------------");
            System.out.println("8: Back\n");
            response = 0;
            
            while(response < 1 || response > 8){
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1){
                    doCreateNewRoomType();
                }
                else if(response == 2){
                    doViewRoomTypeDetails();
                }
                else if(response == 3){
                    doViewAllRoomTypes();
                }
                else if (response == 4){
                    doCreateNewHotelRoom();
                }
                else if (response == 5){
                    doUpdateRoomStatus();
                }
                else if (response == 6){
                    doDeleteRoom();
                }
                else if (response == 7){
                    doViewAllHotelRooms();
                }
                else if (response == 8){
                    break;
                }
                else {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 8){
                break;
            }
        }
    }
    
    
    
    private void doCreateNewRoomType(){
        Scanner scanner = new Scanner(System.in);
        
        String newRoomTypeName = "";
        
        System.out.println("*** Hotel Reservation System Management Client :: Operation Manager :: Create New Room Type ***\n");
        
        System.out.print("Enter New Room Type Name: ");
        newRoomTypeName = scanner.nextLine().trim();
        System.out.print("Is there a Next Higher Room Type? (1: Yes 2: No)> ");
        Integer nextHigherRoomTypeChosen = scanner.nextInt();
        scanner.nextLine();
        
        try {
            if (nextHigherRoomTypeChosen == 2) {
                String createdRoomTypeName = roomTypesEntitySessionBeanRemote.createNewRoomType(new RoomTypes(newRoomTypeName, null)).getRoomTypeName();
                System.out.println("New Room Type created successfully!: " + createdRoomTypeName + "\n");
            }
            else if (nextHigherRoomTypeChosen == 1) {
                System.out.print("Enter Next Higher Room Type: ");
                String nextHigherRoomType = scanner.nextLine().trim();

                //Existing Current Room Types
                List<RoomTypes> existingRoomTypes = roomTypesEntitySessionBeanRemote.retrieveAllRoomTypes();

                for (RoomTypes roomtype : existingRoomTypes) {
                    if (roomtype.getRoomTypeName().equals(nextHigherRoomType)) {
                       String createdRoomTypeName = roomTypesEntitySessionBeanRemote.createNewRoomType(new RoomTypes(newRoomTypeName, roomtype)).getRoomTypeName();
                       System.out.println("New Room Type created successfully: " + createdRoomTypeName + "\n");
                    }
                }
            }
        } catch (RoomTypeAlreadyExistException ex) {
            System.out.println("An error has occurred while creating room type: " + ex.getMessage() + "\n");
        }
    }
    
    
    private void doViewRoomTypeDetails(){
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** Hotel Reservation Management Client System :: Operation Manager :: View Room Type Details ***\n");
        System.out.print("Select Room Type: > ");
        String roomTypeNameChosen = scanner.nextLine().trim();
        
        try {
            RoomTypes roomTypeEntity = roomTypesEntitySessionBeanRemote.retrievesRoomTypeByRoomTypeName(roomTypeNameChosen);
            System.out.printf("%30s%40s%20s%15s%20s%20s%20s\n", "Room Type Name", "Next Higher Room Type", "Description", "Size", "Bed", "Capacity", "Amenities");
            if (roomTypeEntity.getNextHigherRoomType() == null) {
                System.out.printf("%30s%40s%20s%15s%20s%20s%20s\n", roomTypeEntity.getRoomTypeName(), "null", roomTypeEntity.getDescription(), roomTypeEntity.getSize(), roomTypeEntity.getBed(), roomTypeEntity.getCapacity(), roomTypeEntity.getAmenities());
            } else {
                System.out.printf("%30s%40s%20s%15s%20s%20s%20s\n", roomTypeEntity.getRoomTypeName(), roomTypeEntity.getNextHigherRoomType().getRoomTypeName(), roomTypeEntity.getDescription(), roomTypeEntity.getSize(), roomTypeEntity.getBed(), roomTypeEntity.getCapacity(), roomTypeEntity.getAmenities());         
            }
            System.out.println("------------------------");
            System.out.println("1: Update Room Type");
            System.out.println("2: Delete Room Type");
            System.out.println("3: Back\n");
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
            System.out.println("An error has occurred while viewing all room types: " + ex.getMessage() + "\n");
        }
    }
    
    
    private void doUpdateRoomType(RoomTypes roomTypeEntity) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Hotel Reservation Management Client System :: Operation Manager :: View Room Type Details :: Update Room Type ***\n");
        System.out.print("Enter new room type name to be updated: ");
        try {
            String newRoomTypeName = scanner.nextLine().trim();
            roomTypesEntitySessionBeanRemote.updateRoomType(roomTypeEntity, newRoomTypeName);
            System.out.println("Room Type Name " + roomTypeEntity.getRoomTypeName() + " has been successfully updated to " + newRoomTypeName + " !");
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("An error has occurred while updating the room type: " + ex.getMessage() + "\n");
        }
    }
    
    
    private void doDeleteRoomType(RoomTypes roomTypeEntity){
        Scanner scanner = new Scanner(System.in);        
        String input;
        
        System.out.println("*** Hotel Reservation Management Client System :: Operation Manager :: View Room Type Details :: Delete Room Type ***\n");
        System.out.printf("Confirm Delete Room Type %s (Enter 'Y' to Delete)> ", roomTypeEntity.getRoomTypeName());
        input = scanner.nextLine().trim();
        
        if(input.equals("Y")) {
            try {
                roomTypesEntitySessionBeanRemote.deleteRoomType(roomTypeEntity.getRoomTypeName());
                System.out.println("Room Type deleted successfully!\n");
            }
            catch (RoomTypeNotFoundException | DeleteRoomTypeException ex) {
                System.out.println("An error has occurred while deleting the room type: " + ex.getMessage() + "\n");
            }
        }
        else {
            System.out.println("Room Type NOT deleted!\n");
        }
    }
    
    
    private void doViewAllRoomTypes(){
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Hotel Reservation Management Client System :: Operation Manager :: View All Room Types ***\n");
        
        List<RoomTypes> roomTypes = roomTypesEntitySessionBeanRemote.retrieveAllRoomTypes();
        System.out.printf("%s%40s%30s%20s%20s%20s\n", "Room Type Name", "Next Higher Room Type", "Description", "Size", "Bed", "Capacity", "Amenities");

        for (RoomTypes roomType : roomTypes) {
            if (roomType.getNextHigherRoomType() == null) {
                System.out.printf("%s%40s%30s%20s%20s%20s%20s\n", roomType.getRoomTypeName(), "null", roomType.getDescription(), roomType.getSize(), roomType.getBed(), roomType.getCapacity(), roomType.getAmenities());
            } else {
                System.out.printf("%s%40s%30s%20s%20s%20s%20s\n", roomType.getRoomTypeName(), roomType.getNextHigherRoomType().getRoomTypeName(), roomType.getDescription(), roomType.getSize(), roomType.getBed(), roomType.getCapacity(), roomType.getAmenities());
            }
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    
    private void doCreateNewHotelRoom(){
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Hotel Reservation System Management Client :: Operation Manager :: Create New Hotel Room ***\n");
        
        System.out.print("Enter New Room Number: ");
        String newHotelRoomNumber = scanner.nextLine().trim();
        System.out.print("Enter Room Type: ");
        String roomTypeEntered = scanner.nextLine().trim();
        
        try {
            String createdHotelRoomID = hotelRoomsEntitySessionBeanRemote.createNewHotelRoom(new HotelRooms(newHotelRoomNumber, roomTypesEntitySessionBeanRemote.retrievesRoomTypeByRoomTypeName(roomTypeEntered)));
            System.out.println("New Room created successfully!: " + createdHotelRoomID + "\n");
        } catch (RoomAlreadyExistException | RoomTypeNotFoundException ex) {
            System.out.println("An error has occurred while creating new hotel room\n");
        }
    }
    
    
    private void doUpdateRoomStatus() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Hotel Reservation Management Client System :: Operation Manager :: Update Room Status ***\n");
        System.out.print("Enter room number to be updated: ");
        try {
            String roomNumber = scanner.nextLine().trim();
            HotelRooms hotelRoom = hotelRoomsEntitySessionBeanRemote.retrievesHotelRoomByRoomNumber(roomNumber);
            hotelRoomsEntitySessionBeanRemote.updateHotelRoom(hotelRoom);
            System.out.println("Room Number " + hotelRoom.getHotelRoomID() + " status has been successfully updated to not available!");
        } catch (RoomNotFoundException ex) {
            System.out.println("An error has occurred while updating the room: " + ex.getMessage() + "\n");
        }
    }
    
    
    private void doDeleteRoom(){
        Scanner scanner = new Scanner(System.in);        
        String input;
        
        System.out.println("*** Hotel Reservation Management Client System :: Operation Manager :: Delete Room ***\n");
        System.out.print("Enter room number to be deleted: ");
        String roomNumber = scanner.nextLine().trim();
        System.out.printf("Confirm Delete Room Type %s (Enter 'Y' to Delete)> ", roomNumber);
        input = scanner.nextLine().trim();
        
        if(input.equals("Y")) {
            try {
                hotelRoomsEntitySessionBeanRemote.deleteRoom(roomNumber);
                System.out.println("Room deleted successfully!\n");
            }
            catch (RoomNotFoundException | DeleteRoomException ex) {
                System.out.println("An error has occurred while deleting the room: " + ex.getMessage() + "\n");
            }
        }
        else {
            System.out.println("Room NOT deleted!\n");
        }
    }
    
    private void doViewAllHotelRooms(){
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Hotel Reservation Management Client System :: Operation Manager :: View All Hotel Rooms ***\n");
        
        List<HotelRooms> hotelRooms = hotelRoomsEntitySessionBeanRemote.retrieveAllHotelRooms();
        System.out.printf("%s%30s%20s%20s\n", "Room Number", "Allocated Status", "Availability", "Room Type");

        for (HotelRooms hotelRoom : hotelRooms) {
            System.out.printf("%s%30s%20s%30s\n", hotelRoom.getHotelRoomID(), hotelRoom.getIsAllocated(), hotelRoom.getStatus(), hotelRoom.getRmType().getRoomTypeName());
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
}