/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelreservationsclient;

import ejb.session.stateful.AllocationSessionBeanRemote;
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
import util.exception.InvalidDateRangeException;
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
    private AllocationSessionBeanRemote allocationSessionBeanRemote;

    SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
    SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");//"dd/MM/yyyy hh:mm a"

    public ReservationClient() {
    }

    public ReservationClient(HotelReservationSessionBeanRemote hotelReservationSessionBeanRemote, CustomersEntitySessionBeanRemote customersEntitySessionBeanRemote, ReservationsEntitySessionBeanRemote reservationsEntitySessionBeanRemote, AllocationSessionBeanRemote allocationSessionBeanRemote) {
        this.hotelReservationSessionBeanRemote = hotelReservationSessionBeanRemote;
        this.customersEntitySessionBeanRemote = customersEntitySessionBeanRemote;
        this.reservationsEntitySessionBeanRemote = reservationsEntitySessionBeanRemote;
        this.allocationSessionBeanRemote = allocationSessionBeanRemote;
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

            while (response < 1 || response > 5) {
                try {
                    System.out.print("> ");
                                System.out.flush();

                    response = scanner.nextInt();
                    if (response == 1) {
                        if (hotelReservationSessionBeanRemote.isLoggedIn() == false) {
                            try {
                                doLogin();
                                System.out.println("Login successful as " + hotelReservationSessionBeanRemote.getCurrentCustomer().getName() + "!\n");

                            } catch (InvalidLoginCredentialException ex) {
                                System.err.println("\n" + ex.getMessage());
                                break;
                            }
                        } else {
                            System.err.println("You are already login as " + hotelReservationSessionBeanRemote.getCurrentCustomer().getPassportNum() + "\n");
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

                    
                } catch (InputMismatchException ex) {
                    System.err.println("\nInput type mismatch.\n");
                    System.err.flush();
                    scanner.nextLine();
                    break;
                }
            }
            if (response == 5) {
                        hotelReservationSessionBeanRemote.setCurrentCustomer(null);
                        System.out.println("Exited Reservation Client\n");
                        break;
                    }

        }

    }

    private void doViewReservations() {
        try {
            Scanner scanner = new Scanner(System.in);

            Integer response = 0;
            Long reservationID = 0l;
            List<Reservations> reservations = new ArrayList<Reservations>();
            System.out.println("*** Holiday Reservation System :: View Reservations ***\n");
                        System.out.flush();

            reservations = reservationsEntitySessionBeanRemote.retrieveReserationsOfCustomer(hotelReservationSessionBeanRemote.checkLoggedIn());
            System.out.printf("%14s%26s\n", "Reservation ID", "Date");

            for (Reservations r : reservations) {
                //System.out.println(r.getReservationID() +  " " + outputDateFormat.format(r.getStartDate()) + "-" + outputDateFormat.format(r.getEndDate()));
                System.out.printf("%14s%26s\n", r.getReservationID().toString(), outputDateFormat.format(r.getStartDate()) + "-" + outputDateFormat.format(r.getEndDate()));

            }
            System.out.println("");

            System.out.println("1: View Reservation");
            System.out.println("2: Exit");
            while (response < 1 || response > 2) {
                System.out.print("> ");
                response = scanner.nextInt();
                while (response == 1) {
                    System.out.print("Please enter reservation ID> ");
                    reservationID = scanner.nextLong();
                    scanner.nextLine();
                    System.out.printf("%14s%35s      %s%26s\n", "Reservation ID", "Room Type", "Cost", "Date");

                    for (Reservations r : reservations) {
                        if (r.getReservationID().equals(reservationID)) {
                            System.out.printf("%14s%35s       %s%26s\n", r.getReservationID().toString(), r.getReservationRoomType().getRoomTypeName(), r.getCost(), outputDateFormat.format(r.getStartDate()) + "-" + outputDateFormat.format(r.getEndDate()) );
                            //System.out.println(r.getReservationID() + " " + r.getRoomType() + " " + r.getCost());
                            break;
                        }
                    }
                    System.out.println("1: View Reservation");
                    System.out.println("2: Exit");
                    System.out.print("> ");
                    response = scanner.nextInt();

                }

                if (response == 2) {
                    break;
                }

            }
        } catch (NotLoggedInException ex) {
            System.err.println(ex.getMessage() + "\n");
        }

    }

    private void doReserveRoom(Date checkInDate) {
        try {
                        System.out.flush();

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
                        System.out.flush();

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
            System.out.flush();

                            reserveResponse = scanner.nextInt();

                            if (reserveResponse == 1) {

                                List<Reservations> confirmedReservations = hotelReservationSessionBeanRemote.confirmReservations();
                                System.out.printf("%14s%24s%26s%26s\n", "Reservation ID", "Room Type", "Cost", "Date");

                                for(Reservations r: confirmedReservations){
                            System.out.printf("%14s%26s%26s%26s\n", r.getReservationID().toString(), r.getReservationRoomType().getRoomTypeName(), r.getCost(), outputDateFormat.format(r.getStartDate()) + "-" + outputDateFormat.format(r.getEndDate()));
                                }
                                System.out.println("");
                                System.out.println("Reservation(s) Confirmed!\n");
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
                                System.err.println("Cancelled\n");
                                break;
                            }
                        }

                        if (response == 3) {
                            System.err.println("Exited Reservation Function\n");
                            break;
                        }

                    }

                }

            }
        } catch (InvalidRoomTypeException | InvalidRoomQuantityException ex) {
            System.err.println(ex.getMessage());
            handleReservationExceptions(checkInDate);
        } catch (NotLoggedInException ex) {
            System.err.println(ex.getMessage());
        }

    }

    private void handleReservationExceptions(Date checkInDate) {
        //MAYBE DO TYPE IN ROOM NAME + QUANTITY
        Scanner scanner = new Scanner(System.in);
        System.out.println("");
        Integer response = 0;
        System.out.println("1: Try Again");
        System.out.println("2: Exit");
        while (response < 1 || response > 2) {
            System.out.print("> ");
            response = scanner.nextInt();
            if (response == 1) {
                doReserveRoom(checkInDate);
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
            System.out.flush();

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
            System.out.flush();

            Map<RoomTypes, List<Integer>> availability = hotelReservationSessionBeanRemote.searchHotelRooms(checkInDate, checkOutDate);
            System.out.println("*** Showing available rooms for: " + outputDateFormat.format(checkInDate) + " to " + outputDateFormat.format(checkOutDate) + "***\n");

            System.out.printf("%25s%16s%9s\n", "Room Type", "Availability", "Price" );

            availability.entrySet().forEach(rooms -> {
                System.out.printf("%25s%16s%9s\n", rooms.getKey().getRoomTypeName(), rooms.getValue().get(0), rooms.getValue().get(1));

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
                    doReserveRoom(checkInDate);

                }
            }

        } catch (ParseException ex) {
            System.err.println("\nInvalid date input!\n");
            System.err.flush();

        } catch (InvalidDateRangeException ex) {
            System.err.println(ex.getMessage());
            System.err.flush();

        }

    }

    private void doRegister() throws InvalidLoginCredentialException {
        Scanner scanner = new Scanner(System.in);
        Long passportNumber = 0l;
        String password = "";
        String name = "";

        System.out.println("*** Holiday Reservation System :: Register ***\n");
        System.out.print("Enter name> ");
        name = scanner.nextLine().trim();

        System.out.print("Enter passport number> ");
        passportNumber = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (passportNumber > 0 && password.length() > 0) {
            try {
                Customers currentCustomer = customersEntitySessionBeanRemote.createNewCustomer(new Customers(passportNumber, name, password));
                System.out.println("");
                System.out.println("Successfully registered " + currentCustomer.getName()+  " with passport number "+ currentCustomer.getPassportNum() + "!\n");
                System.out.println("Automatically loggin in...\n");
                System.out.println("Login successful as " + currentCustomer.getName() + "!\n");
                            System.out.flush();

                hotelReservationSessionBeanRemote.login(currentCustomer);

            } catch (UserAlreadyExistException ex) {
                System.err.println("User already registered.");
            }
        } else {
            throw new InvalidLoginCredentialException("Invalid input !");
        }
    }

}
