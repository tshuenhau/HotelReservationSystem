package hotelreservationsystemclient;

import ejb.session.stateful.AllocationSessionBeanRemote;
import ejb.session.stateful.HotelReservationSessionBeanRemote;
import entity.Employees;
import java.util.Scanner;
import ejb.session.stateless.EmployeesEntitySessionBeanRemote;
import ejb.session.stateless.HotelRoomsEntitySessionBeanRemote;
import ejb.session.stateless.RatesEntitySessionBeanRemote;
import ejb.session.stateless.ReservationsEntitySessionBeanRemote;
import ejb.session.stateless.RoomTypesEntitySessionBeanRemote;
import entity.HotelRooms;
import entity.Reservations;
import entity.RoomTypes;
import java.util.List;
import util.exception.InvalidAccessRightException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Map;
import util.exception.InvalidRoomQuantityException;
import util.exception.InvalidRoomTypeException;
import util.exception.NotLoggedInException;
import util.exception.UnableToAllocateException;



public class GuestRelationOfficerModule {
    private EmployeesEntitySessionBeanRemote employeesEntitySessionBeanRemote;
    private RoomTypesEntitySessionBeanRemote roomTypesEntitySessionBeanRemote;
    private HotelRoomsEntitySessionBeanRemote hotelRoomsEntitySessionBeanRemote;
    private RatesEntitySessionBeanRemote ratesEntitySessionBeanRemote;
    private HotelReservationSessionBeanRemote hotelReservationSessionBeanRemote;
    private AllocationSessionBeanRemote allocationSessionBeanRemote;
    private ReservationsEntitySessionBeanRemote reservationsEntitySessionBeanRemote;
    
    private Employees currentEmployee;
    
    SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");//"dd/MM/yyyy hh:mm a"
    
    public GuestRelationOfficerModule(){
    }

    public GuestRelationOfficerModule(AllocationSessionBeanRemote allocationSessionBeanRemote, EmployeesEntitySessionBeanRemote employeesEntitySessionBeanRemote, Employees currentEmployee, RoomTypesEntitySessionBeanRemote roomTypesEntitySessionBeanRemote, HotelRoomsEntitySessionBeanRemote hotelRoomsEntitySessionBeanRemote, RatesEntitySessionBeanRemote ratesEntitySessionBeanRemote, HotelReservationSessionBeanRemote hotelReservationSessionBeanRemote, ReservationsEntitySessionBeanRemote reservationsEntitySessionBeanRemote) {
        this.employeesEntitySessionBeanRemote = employeesEntitySessionBeanRemote;
        this.allocationSessionBeanRemote = allocationSessionBeanRemote;
        this.roomTypesEntitySessionBeanRemote = roomTypesEntitySessionBeanRemote;
        this.hotelRoomsEntitySessionBeanRemote = hotelRoomsEntitySessionBeanRemote;
        this.ratesEntitySessionBeanRemote = ratesEntitySessionBeanRemote;
        this.currentEmployee = currentEmployee;
        this.hotelReservationSessionBeanRemote = hotelReservationSessionBeanRemote;
        this.reservationsEntitySessionBeanRemote = reservationsEntitySessionBeanRemote;
    }
    
    
    
    public void menuGuestRelationOfficer() throws InvalidAccessRightException {
        if(!currentEmployee.getEmployeeType().equals("Guest Relation Officer")){
            throw new InvalidAccessRightException("You don't have Guest Relation Officer rights to access the guest relation officer module.");
        }
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        try {
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
                        doWalkInSearch();
                    }
                    else if(response == 2){
                        try {
                            doCheckIn();
                        } catch (UnableToAllocateException ex) {
                            System.out.println("Unable to allocate room. No available room");
                        }
                    }
                    else if(response == 3){
                        //doCheckOut();
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
    
    
    private void doWalkInSearch() {
        try {
            Scanner scanner = new Scanner(System.in);
            Integer response = 0;

            Date checkInDate;
            Date checkOutDate;

            System.out.println("*** Hotel Reservation System Management Client :: Walk-in Search Room ***\n");

            System.out.print("Enter Check-In Date (dd/mm/yyyy)> ");
            checkInDate = inputDateFormat.parse(scanner.nextLine().trim());
            System.out.print("Enter Check-Out Date (dd/mm/yyyy)> ");
            checkOutDate = inputDateFormat.parse(scanner.nextLine().trim());

            System.out.println("*** Showing available rooms for: " + outputDateFormat.format(checkInDate) + " to " + outputDateFormat.format(checkOutDate) + "***\n");

            System.out.printf("%12s%16s%9s\n", "Room Type", "Availability", "Price");
            Map<RoomTypes, List<Integer>> availability = hotelReservationSessionBeanRemote.walkInSearchHotelRooms(checkInDate, checkOutDate);

            availability.entrySet().forEach(rooms -> {
                System.out.printf("%12s%16s%9s\n", rooms.getKey().getRoomTypeName(), rooms.getValue().get(0), rooms.getValue().get(1));

                //System.out.println(rooms.getKey() + "      " + rooms.getValue().get(0) + "    " + rooms.getValue().get(1));
            });
            System.out.println("");

            System.out.println("Reserve Room?\n");
            System.out.println("1: Reserve Room");
            System.out.println("2: Exit");
            try {
                while (response < 1 || response > 2) {
                    System.out.print("> ");
                    response = scanner.nextInt();
                    if (response == 1) {
                        hotelReservationSessionBeanRemote.walkInLogin();
                        doReserveRoom(checkInDate);
                    }
                }
            } catch (InputMismatchException ex) {
                scanner.nextLine();
                System.err.println("Input Mismatch.");
            }

        } catch (ParseException ex) {
            System.err.println("Invalid date input!\n");
        }
    }
    
    private void doReserveRoom(Date checkInDate) {
        try {
            hotelReservationSessionBeanRemote.checkLoggedIn();
            //MAYBE DO TYPE IN ROOM NAME + QUANTITY
            Scanner scanner = new Scanner(System.in);

            Integer response = 0;
            String roomType = "";
            List<Reservations> reservations = new ArrayList<>();
            Integer quantity = 0;

            System.out.println("*** Holiday Reservation System :: Reserve Hotel Room ***\n");
            while (response < 1 || response > 3) {
                response = 1;
                while (response == 1) {
                    System.out.print("Please enter hotel room type> ");
                    roomType = scanner.nextLine();
                    System.out.print("Please enter quantity> ");
                    quantity = scanner.nextInt();
                    scanner.nextLine();
                    try {
                        reservations = hotelReservationSessionBeanRemote.addReservation(roomType, quantity);
                    } catch (NotLoggedInException ex) {
                        System.err.println(ex.getMessage());
                    }
                    System.out.println("");

                    System.out.println("1: Add more rooms");
                    System.out.println("2: Confirm");
                    System.out.println("3: Cancel");
                    response = 0;
                    while (response < 1 || response > 3) {
                        System.out.print("> ");
                        response = scanner.nextInt();
                        scanner.nextLine();
                        if (response == 2) {
                            Map<RoomTypes, Integer> quantities = hotelReservationSessionBeanRemote.getRoomQuantities();
                            Integer totalCost = hotelReservationSessionBeanRemote.getTotalCost();

                            System.out.println("Confirm the following reservations?\n");

                            quantities.forEach(
                                    (key, value) -> System.out.println(key.getRoomTypeName() + " X " + value)
                            );

                            System.out.println("Total Cost: $" + totalCost);
                            System.out.println("");

//                            if (reservations != null) {
//                                for (Reservations r : reservations) {
//                                    System.out.println(r.getReservationID() + " " + r.getRoomType());
//                                }
//                            }
                            int reserveResponse = 0;
                            System.out.println("1: Confirm");
                            System.out.println("2: Cancel");
                            System.out.print("> ");
                            reserveResponse = scanner.nextInt();
                            if (reserveResponse == 1) {

                                List<Reservations> reserved = hotelReservationSessionBeanRemote.confirmReservations();
                                
                                System.out.printf("%s%30s\n", "Rservation ID", "Room Type");
                                for (Reservations reserve : reserved) {
                                    System.out.printf("%s%30s\n", reserve.getReservationID(), reserve.getReservationRoomType().getRoomTypeName());
                                }
                                System.out.println("Confirmed\n");
                                System.out.println("Same day check-in (and after 2am)?");
                                System.out.println("1: Yes");
                                System.out.println("2: No");
                                System.out.print("> ");
                                reserveResponse = scanner.nextInt();
                                scanner.nextLine();
                                if(reserveResponse ==1 ){
                                    allocationSessionBeanRemote.allocateRooms(checkInDate);
                                    break;
                                }
                                
                            } else if (reserveResponse == 2) {
                                System.out.println("Cancelled\n");
                                break;
                            }
                        }

                        if (response == 3) {
                            System.out.println("Exited Reservation Function\n");
                            break;
                        }
                    }
                }
            }
        } catch (InvalidRoomTypeException | InvalidRoomQuantityException ex) {
            System.out.println(ex.getMessage());
            handleReservationExceptions(checkInDate);
        } catch (NotLoggedInException ex) {
            System.err.println(ex.getMessage());
        }

    }
    
    private void handleReservationExceptions(Date checkInDate) {
        //MAYBE DO TYPE IN ROOM NAME + QUANTITY
        Scanner scanner = new Scanner(System.in);

        Integer response = 0;
        System.out.println("1: Try Again");
        System.out.println("2: Exit");
        try {
            while (response < 1 || response > 2) {
                System.out.print("> ");
                response = scanner.nextInt();
                if (response == 1) {
                    doReserveRoom(checkInDate);
                }
            }
        } catch (InputMismatchException ex) {
                scanner.nextLine();
                System.err.println("Input Mismatch.");
        }
    }
    
    private void doCheckIn() throws UnableToAllocateException  {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Hotel Reservation System Management Client :: Check-in Guest ***\n");
        
        System.out.print("Enter reservation ID> ");
        Long reservationID = scanner.nextLong();
        scanner.nextLine();
        
        Reservations reservation = reservationsEntitySessionBeanRemote.retrieveReservation(reservationID);
        HotelRooms allocatedRoom = reservation.getAllocatedRoom();
        if (allocatedRoom != null) {
            System.out.println("You have been allocated to Room " + allocatedRoom.getHotelRoomID() + " at " + allocatedRoom.getRmType().getRoomTypeName());
        } else {
            throw new UnableToAllocateException();
        }
    }
    /*
    private void doCheckOut() {
        System.out.println("*** Hotel Reservation System Management Client :: Check-out Guest ***\n");
    }
*/

}