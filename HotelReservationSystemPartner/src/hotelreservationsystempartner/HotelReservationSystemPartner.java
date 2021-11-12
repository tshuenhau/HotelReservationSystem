/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelreservationsystempartner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import ws.client.Customers;
import ws.client.InvalidLoginCredentialException_Exception;
import ws.client.InvalidRoomQuantityException_Exception;
import ws.client.InvalidRoomTypeException_Exception;
import ws.client.ParseException_Exception;
import ws.client.ReservationWebService_Service;
import ws.client.Reservations;
import ws.client.StringArray;
//service.getReservationWebServicePort();

/**
 *
 * @author tshuenhau
 */
public class HotelReservationSystemPartner {

    static SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
    static SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");//"dd/MM/yyyy hh:mm a"

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Customers currentCustomer = null;
        ReservationWebService_Service service = new ReservationWebService_Service();

        // TODO code application logic here
        Scanner scanner = new Scanner(System.in);

        Integer response = 0;
        while (true) {
            System.out.println("*** Welcome to Holiday Reservation Client ***\n");

            response = 0;
            System.out.println("1: Login");
            System.out.println("2: Search Hotel Room");
            System.out.println("3: View Reservations");

            System.out.println("4: Exit\n");
            while (response < 1 || response > 4) {

                System.out.print("> ");
                response = scanner.nextInt();
                if (response == 1) {
                    Long username = 0l;
                    String password = "";
                    System.out.println("*** Holiday Reservation System :: Login ***\n");

                    System.out.print("Enter username> ");
                    username = scanner.nextLong();
                    scanner.nextLine();
                    System.out.print("Enter password> ");
                    password = scanner.nextLine().trim();
                    try {
                        currentCustomer = service.getReservationWebServicePort().login(username, password);
                    } catch (InvalidLoginCredentialException_Exception ex) {
                        System.err.println(ex.getMessage());
                    }
                } else if (response == 2) {
                    try {
                        response = 0;
                        scanner.nextLine();
                        String checkInDate;
                        String checkOutDate;

                        System.out.println("*** Holiday Reservation System :: Search Hotel Room ***\n");

                        System.out.print("Enter Check-In Date (dd/mm/yyyy)> ");
                        checkInDate = scanner.nextLine().trim();
                        System.out.print("Enter Check-Out Date (dd/mm/yyyy)> ");
                        checkOutDate = scanner.nextLine().trim();
                        List<StringArray> rooms = service.getReservationWebServicePort().searchRoom(checkInDate, checkOutDate);
                        System.out.printf("%12s%16s%9s\n", "Room Type", "Availability", "Price");
                        for (StringArray s : rooms) {
                            //rooms.get(0)
                            System.out.printf("%12s%16s%9s\n", s.getItem().get(0), s.getItem().get(1), s.getItem().get(2));
                        }
                        System.out.println("");

                        while (response < 1 || response > 2) {
                            System.out.println("Reserve Room?\n");
                            System.out.println("1: Reserve Room");
                            System.out.println("2: Exit");
                            System.out.print("> ");
                            response = scanner.nextInt();
                            if (response == 1) {
                                //doReserveRoom();
                                if (currentCustomer == null) {
                                    System.err.println("Please Login To Use this feature.");
                                    break;
                                } else {
                                    System.out.println("*** Holiday Reservation System :: Reserve Hotel Room ***\n");
                                    String roomType = "";
                                    Integer quantity = 0;
                                    scanner.nextLine();
                                    System.out.print("Please enter hotel room type> ");
                                    roomType = scanner.nextLine();
                                    System.out.print("Please enter quantity> ");
                                    quantity = scanner.nextInt();
                                    scanner.nextLine();
                                    service.getReservationWebServicePort().addReservation(currentCustomer.getPassportNum(), currentCustomer.getPassword(), checkInDate, checkOutDate, roomType, quantity);
                                    System.out.println("Reservation Successful!");
                                    response = 0;
                                }

                            }
                        }

                    } catch (ParseException_Exception ex) {
                        System.err.println(ex.getMessage());
                    } catch (InvalidLoginCredentialException_Exception ex) {
                        System.err.println(ex.getMessage());
                    } catch (InvalidRoomQuantityException_Exception ex) {
                        System.err.println(ex.getMessage());
                    } catch (InvalidRoomTypeException_Exception ex) {
                        System.err.println(ex.getMessage());
                    }

                } else if (response == 3) {
                    try {
                        List<Reservations> reservations = service.getReservationWebServicePort().viewAllReservations(currentCustomer.getPassportNum(), currentCustomer.getPassword());
                        System.out.printf("%14s%26s\n", "Reservation ID", "Date");

                        for (Reservations r : reservations) {
                            //System.out.println(r.getReservationID() +  " " + outputDateFormat.format(r.getStartDate()) + "-" + outputDateFormat.format(r.getEndDate()));
                            System.out.printf("%14s%26s\n", r.getReservationID().toString(), outputDateFormat.format(r.getStartDate()) + "-" + outputDateFormat.format(r.getEndDate()));

                        }
                    } catch (InvalidLoginCredentialException_Exception ex) {
                        System.err.println(ex.getMessage());
                    }
                }

            }
        }

    }

}
