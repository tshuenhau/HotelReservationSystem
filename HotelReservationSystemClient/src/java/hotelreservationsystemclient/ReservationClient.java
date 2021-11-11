/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelreservationsystemclient;

import ejb.session.stateful.HotelReservationSessionBeanRemote;
import ejb.session.stateless.CustomersEntitySessionBeanRemote;
import ejb.session.stateless.ReservationsEntitySessionBeanRemote;
import entity.Customers;
import entity.Reservations;
import entity.RoomTypes;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exception.InvalidLoginCredentialException;
import util.exception.InvalidRoomQuantityException;
import util.exception.InvalidRoomTypeException;
import util.exception.NotLoggedInException;
import util.exception.UserAlreadyExistException;

/**
 *
 * @author tshuenhau
 */
public class ReservationClient {

    private HotelReservationSessionBeanRemote hotelReservationSessionBeanRemote;
    private CustomersEntitySessionBeanRemote customersEntitySessionBeanRemote;
    private ReservationsEntitySessionBeanRemote reservationsEntitySessionBeanRemote;

            SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");//"dd/MM/yyyy hh:mm a"
    public ReservationClient() {
    }

    public ReservationClient(HotelReservationSessionBeanRemote hotelReservationSessionBeanRemote, CustomersEntitySessionBeanRemote customersEntitySessionBeanRemote, ReservationsEntitySessionBeanRemote reservationsEntitySessionBeanRemote) {
        this.hotelReservationSessionBeanRemote = hotelReservationSessionBeanRemote;
        this.customersEntitySessionBeanRemote = customersEntitySessionBeanRemote;
        this.reservationsEntitySessionBeanRemote = reservationsEntitySessionBeanRemote;
    }

    public void runApp() {
        Scanner scanner = new Scanner(System.in);

        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to Holiday Reservation Client ***\n");

            response = 0;
            System.out.println("1: Login");
            System.out.println("2: Register");
            System.out.println("3: Search Hotel Room");
            System.out.println("4: View Reservations");

            System.out.println("5: Exit\n");

            try{while (response < 1 || response > 5) {
                System.out.print("> ");
                response = scanner.nextInt();
                if (response == 1) {
                    if (hotelReservationSessionBeanRemote.getCurrentCustomer() == null) {
                        try {
                            doLogin();
                            System.out.println("Login successful as " + hotelReservationSessionBeanRemote.getCurrentCustomer().getPassportNum() + "!\n");

                        } catch (InvalidLoginCredentialException ex) {
                            System.err.println("Invalid login credential: " + ex.getMessage() + "\n");

                        }
                    } else {
                        System.out.println("You are already login as " + hotelReservationSessionBeanRemote.getCurrentCustomer().getPassportNum() + "\n");
                    }
                }

                if (response == 2) {
                    try {
                        doRegister();
                    } catch (InvalidLoginCredentialException ex) {
                        Logger.getLogger(ReservationClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (response == 3) {
                    doSearchRoom();
                } else if (response == 4) {
                        doViewReservations();
                    
                    
                }

            }

            if (response == 5) {
                hotelReservationSessionBeanRemote.setCurrentCustomer(null);
                System.out.println("Exited Reservation Client\n");
                break;
            }
        }
            catch(InputMismatchException ex){
                System.err.println("Input type mismatch.");
                }
        }
        
    }
    
    private void doViewReservations(){
        try {
            Scanner scanner = new Scanner(System.in);
            
            Integer response = 0;
            Long reservationID = 0l;
            List<Reservations> reservations = new ArrayList<Reservations>();
            System.out.println("*** Holiday Reservation System :: View Reservations ***\n");
            reservations = reservationsEntitySessionBeanRemote.retrieveReserationsOfCustomer(hotelReservationSessionBeanRemote.checkLoggedIn());
            System.out.printf("%14s%26s\n", "Reservation ID", "Date");
            
            for(Reservations r: reservations){
                //System.out.println(r.getReservationID() +  " " + outputDateFormat.format(r.getStartDate()) + "-" + outputDateFormat.format(r.getEndDate()));
                System.out.printf("%14s%26s\n", r.getReservationID().toString(), outputDateFormat.format(r.getStartDate())+ "-" + outputDateFormat.format(r.getEndDate()));
                
            }
            System.out.println("");
            
            System.out.println("1: View Reservation");
            System.out.println("2: Exit");
            while (response < 1 || response > 2){
                System.out.print("> ");
                response = scanner.nextInt();
                while(response == 1) {
                    System.out.print("Please enter reservation ID> ");
                    reservationID = scanner.nextLong();
                    scanner.nextLine();
                    System.out.printf("%14s%16s      %s\n", "Reservation ID", "Room Type", "Cost");

                    for(Reservations r: reservations){
                        if(r.getReservationID().equals(reservationID)){
                            System.out.printf("%14s%16s      %s\n", r.getReservationID().toString(), r.getReservationRoomType().getRoomTypeName(), r.getCost());
                            //System.out.println(r.getReservationID() + " " + r.getRoomType() + " " + r.getCost());
                        }
                    }
                    System.out.println("1: View Reservation");
                    System.out.println("2: Exit");
                    System.out.print("> ");
                    response = scanner.nextInt();
                    
                }
                
                if(response == 2){
                    break;
                }
                
                
            }
        } catch (NotLoggedInException ex) {
            System.err.println(ex.getMessage());
        }

    }

    private void doReserveRoom() {
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
                            reserveResponse = scanner.nextInt();
                            if (reserveResponse == 1) {

                                hotelReservationSessionBeanRemote.confirmReservations();
                                System.out.println("Confirmed\n");

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
            handleReservationExceptions();
        } catch (NotLoggedInException ex) {
            System.err.println(ex.getMessage());
        }

    }

    private void handleReservationExceptions() {
        //MAYBE DO TYPE IN ROOM NAME + QUANTITY
        Scanner scanner = new Scanner(System.in);

        Integer response = 0;
        System.out.println("1: Try Again");
        System.out.println("2: Exit");
        while (response < 1 || response > 2) {
            System.out.print("> ");
            response = scanner.nextInt();
            if (response == 1) {
                doReserveRoom();
            }
        }

    }

    private void doLogin() throws InvalidLoginCredentialException {
        Scanner scanner = new Scanner(System.in);
        Long passportNumber = 0l;
        String password = "";

        System.out.println("*** Holiday Reservation System :: Login ***\n");
        System.out.print("Enter passport number> ");
        passportNumber = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (passportNumber > 0 && password.length() > 0) {
            Customers currentCustomer = customersEntitySessionBeanRemote.login(passportNumber, password);
            hotelReservationSessionBeanRemote.login(currentCustomer);
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }

    private void doSearchRoom() {
        try {
            Scanner scanner = new Scanner(System.in);
            Integer response = 0;

            Date checkInDate;
            Date checkOutDate;

            System.out.println("*** Holiday Reservation System :: Search Hotel Room ***\n");

            System.out.print("Enter Check-In Date (dd/mm/yyyy)> ");
            checkInDate = inputDateFormat.parse(scanner.nextLine().trim());
            System.out.print("Enter Check-Out Date (dd/mm/yyyy)> ");
            checkOutDate = inputDateFormat.parse(scanner.nextLine().trim());

            System.out.println("*** Showing available rooms for: " + outputDateFormat.format(checkInDate) + " to " + outputDateFormat.format(checkOutDate) + "***\n");

            System.out.printf("%12s%16s%9s\n", "Room Type", "Availability", "Price");
            Map<RoomTypes, List<Integer>> availability = hotelReservationSessionBeanRemote.searchHotelRooms(checkInDate, checkOutDate);

            availability.entrySet().forEach(rooms -> {
                System.out.printf("%12s%16s%9s\n", rooms.getKey().getRoomTypeName(), Math.max(rooms.getValue().get(0), 0), rooms.getValue().get(1));

                //System.out.println(rooms.getKey() + "      " + rooms.getValue().get(0) + "    " + rooms.getValue().get(1));
            });
            System.out.println("");

            System.out.println("Reserve Room?\n");
            System.out.println("1: Reserve Room");
            System.out.println("2: Exit");
            while (response < 1 || response > 2) {
                System.out.print("> ");
                response = scanner.nextInt();
                if (response == 1) {
                    doReserveRoom();

                    
                }
            }

        } catch (ParseException ex) {
            System.err.println("Invalid date input!\n");
        }

    }

    private void doRegister() throws InvalidLoginCredentialException{
       Scanner scanner = new Scanner(System.in);
        Long passportNumber = 0l;
        String password = "";

        System.out.println("*** Holiday Reservation System :: Register ***\n");
        System.out.print("Enter passport number> ");
        passportNumber = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (passportNumber > 0 && password.length() > 0) {
           try {
               Customers currentCustomer = customersEntitySessionBeanRemote.createNewCustomer(new Customers(passportNumber, password));
               System.out.println("");
               System.out.println("Successfully registered " + currentCustomer.getPassportNum() + "!\n");
                System.out.println("Automatically loggin in...\n");
               System.out.println("Login successful as " + currentCustomer.getPassportNum() + "!\n");
                       hotelReservationSessionBeanRemote.login(currentCustomer);

           } catch (UserAlreadyExistException ex) {
               System.err.println("User already registered.");
           }
        } else {
            throw new InvalidLoginCredentialException("Invalid input !");
        }
    }

}
