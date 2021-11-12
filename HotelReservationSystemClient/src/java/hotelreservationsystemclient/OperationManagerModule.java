package hotelreservationsystemclient;

import entity.Employees;
import java.util.Scanner;
import ejb.session.stateless.EmployeesEntitySessionBeanRemote;
import ejb.session.stateless.HotelRoomsEntitySessionBeanRemote;
import ejb.session.stateless.RoomTypesEntitySessionBeanRemote;
import entity.HotelRooms;
import entity.RoomTypes;
import java.util.List;
import util.exception.DeleteRoomTypeException;
import util.exception.InvalidAccessRightException;
import util.exception.RoomTypeNotFoundException;



public class OperationManagerModule {
    private EmployeesEntitySessionBeanRemote employeesEntitySessionBeanRemote;
    private RoomTypesEntitySessionBeanRemote roomTypesEntitySessionBeanRemote;
    private HotelRoomsEntitySessionBeanRemote hotelRoomsEntitySessionBeanRemote;
    
    private Employees currentEmployee;

    
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
            System.out.println("5: Update Room");
            System.out.println("6: Delete Room");
            System.out.println("7: View All Rooms");
            System.out.println("-----------------------");
            System.out.println("8: View Room Allocation Exception Report");
            System.out.println("-----------------------");
            System.out.println("9: Back\n");
            response = 0;
            
            while(response < 1 || response > 9){
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
                   // doUpdateNewHotelRoom();
                }
                else if (response == 6){
                    //doViewAllHotelRooms();
                }
                else if (response == 7){
                    doViewAllHotelRooms();
                }
                else if (response == 8){
                    doViewAllocationReport();
                }
                else if (response == 9){
                    break;
                }
                else {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 9){
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
        
        if (nextHigherRoomTypeChosen == 2) {
            String createdRoomTypeName = roomTypesEntitySessionBeanRemote.createNewRoomType(new RoomTypes(newRoomTypeName, null)).getRoomTypeName();
            System.out.println("New Room Type created successfully!: " + createdRoomTypeName + "\n");
        }
        else if (nextHigherRoomTypeChosen == 1) {
            System.out.print("Select Next Higher Room Type (1: Deluxe Room, 2: Premier Room, 3: Family Room, 4: Junior Suite, 5: Grand Suite)> ");
            Integer roomTypeChosen = scanner.nextInt();
            
            //Existing Current Room Types
            RoomTypes GrandSuite = new RoomTypes("Grand Suite", null);
            RoomTypes JuniorSuite = new RoomTypes("Junior Suite", GrandSuite);
            RoomTypes FamilyRoom = new RoomTypes("Family Room", JuniorSuite);
            RoomTypes PremierRoom = new RoomTypes("Premier Room", FamilyRoom);
            RoomTypes DeluxeRoom = new RoomTypes("Deluxe Room", PremierRoom);

            while (true) {
                if (roomTypeChosen == 1) {
                    String createdRoomTypeName = roomTypesEntitySessionBeanRemote.createNewRoomType(new RoomTypes(newRoomTypeName, DeluxeRoom)).getRoomTypeName();
                    System.out.println("New Room Type created successfully!: " + createdRoomTypeName + "\n");
                    break;
                } else if (roomTypeChosen == 2) {
                    String createdRoomTypeName = roomTypesEntitySessionBeanRemote.createNewRoomType(new RoomTypes(newRoomTypeName, PremierRoom)).getRoomTypeName();
                    System.out.println("New Room Type created successfully!: " + createdRoomTypeName + "\n");
                    break;
                } else if (roomTypeChosen == 3) {
                    String createdRoomTypeName = roomTypesEntitySessionBeanRemote.createNewRoomType(new RoomTypes(newRoomTypeName, FamilyRoom)).getRoomTypeName();
                    System.out.println("New Room Type created successfully!: " + createdRoomTypeName + "\n");
                    break;
                } else if (roomTypeChosen == 4) {
                    String createdRoomTypeName = roomTypesEntitySessionBeanRemote.createNewRoomType(new RoomTypes(newRoomTypeName, JuniorSuite)).getRoomTypeName();
                    System.out.println("New Room Type created successfully!: " + createdRoomTypeName + "\n");
                    break;
                } else if (roomTypeChosen == 5) {
                    String createdRoomTypeName = roomTypesEntitySessionBeanRemote.createNewRoomType(new RoomTypes(newRoomTypeName, GrandSuite)).getRoomTypeName();
                    System.out.println("New Room Type created successfully!: " + createdRoomTypeName + "\n");
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
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
            System.out.printf("%10s%30s%20s%15s%20s%20s%20s\n", "Room Type Name", "Next Higher Room Type", "Description", "Size", "Bed", "Capacity", "Amenities");
            if (roomTypeEntity.getNextHigherRoomType() == null) {
                System.out.printf("%10s%30s%20s%15s%20s%20s%20s\n", roomTypeEntity.getRoomTypeName(), "null", roomTypeEntity.getDescription(), roomTypeEntity.getSize(), roomTypeEntity.getBed(), roomTypeEntity.getCapacity(), roomTypeEntity.getAmenities());
            } else {
                System.out.printf("%10s%30s%20s%15s%20s%20s%20s\n", roomTypeEntity.getRoomTypeName(), roomTypeEntity.getNextHigherRoomType().getRoomTypeName(), roomTypeEntity.getDescription(), roomTypeEntity.getSize(), roomTypeEntity.getBed(), roomTypeEntity.getCapacity(), roomTypeEntity.getAmenities());         
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
            System.out.println("An error has occurred while retrieving staff: " + ex.getMessage() + "\n");
        }
    }
    
    
    private void doUpdateRoomType(RoomTypes roomTypeEntity) {
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
    
    
    private void doDeleteRoomType(RoomTypes roomTypeEntity){
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
    
    
    private void doViewAllRoomTypes(){
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Hotel Reservation Management Client System :: Operation Manager :: View All Room Types ***\n");
        
        List<RoomTypes> roomTypes = roomTypesEntitySessionBeanRemote.retrieveAllRoomTypes();
        System.out.printf("%s%30s%20s%20s%20s%20s\n", "Room Type Name", "Description", "Size", "Bed", "Capacity", "Amenities");

        for (RoomTypes roomType : roomTypes) {
            System.out.printf("%s%30s%20s%20s%20s%20s\n", roomType.getRoomTypeName(), roomType.getDescription(), roomType.getSize(), roomType.getBed(), roomType.getCapacity(), roomType.getAmenities());
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    
    private void doCreateNewHotelRoom(){
        Scanner scanner = new Scanner(System.in);
        
        String newHotelRoomNumber = "";
        
        System.out.println("*** Hotel Reservation System Management Client :: Operation Manager :: Create New Hotel Room ***\n");
        
        System.out.print("Enter New Room Number: ");
        newHotelRoomNumber = scanner.nextLine().trim();
        System.out.print("Enter Room Type: ");
        String roomTypeEntered = scanner.nextLine().trim();
        
        try {
            String createdHotelRoomID = hotelRoomsEntitySessionBeanRemote.createNewHotelRoom(new HotelRooms(newHotelRoomNumber, roomTypesEntitySessionBeanRemote.retrievesRoomTypeByRoomTypeName(roomTypeEntered)));
            System.out.println("New Room created successfully!: " + createdHotelRoomID + "\n");
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("An error has occurred while creating new hotel room\n");
        }
    }
    
    private void doViewAllocationReport(){
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Hotel Reservation Management Client System :: Operation Manager :: View Room Allocation Exception Report ***\n");
        
        List<HotelRooms> hotelRooms = hotelRoomsEntitySessionBeanRemote.retrieveAllHotelRooms();
        System.out.printf("%s%30s%20s%20s\n", "Room Number", "Allocated Status", "Availability", "Room Type");

        for (HotelRooms hotelRoom : hotelRooms) {
            System.out.printf("%s%30s%20s%20s\n", hotelRoom.getHotelRoomID(), hotelRoom.getIsAllocated(), hotelRoom.getStatus(), hotelRoom.getRmType().getRoomTypeName());
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    
    private void doViewAllHotelRooms(){
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Hotel Reservation Management Client System :: Operation Manager :: View All Hotel Rooms ***\n");
        
        List<HotelRooms> hotelRooms = hotelRoomsEntitySessionBeanRemote.retrieveAllHotelRooms();
        System.out.printf("%s%30s%20s%20s\n", "Room Number", "Allocated Status", "Availability", "Room Type");

        for (HotelRooms hotelRoom : hotelRooms) {
            System.out.printf("%s%30s%20s%20s\n", hotelRoom.getHotelRoomID(), hotelRoom.getIsAllocated(), hotelRoom.getStatus(), hotelRoom.getRmType().getRoomTypeName());
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
}