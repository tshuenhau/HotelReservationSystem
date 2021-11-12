/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelreservationsystempartner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.client.Customers;
import ws.client.InvalidLoginCredentialException_Exception;
import ws.client.InvalidRoomQuantityException_Exception;
import ws.client.InvalidRoomTypeException_Exception;
import ws.client.ParseException_Exception;
import ws.client.ReservationNotFoundException_Exception;
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
            System.out.println("*** Welcome to Holiday Reservation Partner Client ***\n");

            response = 0;
            System.out.println("1: Login");
            System.out.println("2: Search Hotel Room");
            System.out.println("3: View All Reservations");
            System.out.println("4: View Reservation");

            System.out.println("5: Exit\n");
            while (response < 1 || response > 5) {
                try {
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
                        currentCustomer = service.getReservationWebServicePort().login(username, password);
                        System.out.println();
                        System.out.println("Logged in successfully as: " + currentCustomer.getName());
                        System.out.println();

                        

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
                            System.out.printf("%35s%16s%9s\n", "Room Type", "Availability", "Price");
                            for (StringArray s : rooms) {
                                //rooms.get(0)
                                System.out.printf("%35s%16s%9s\n", s.getItem().get(0), s.getItem().get(1), s.getItem().get(2));
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
                                        try {
                                            service.getReservationWebServicePort().addReservation(currentCustomer.getPassportNum(), currentCustomer.getPassword(), checkInDate, checkOutDate, roomType, quantity);
                                        } catch (InvalidRoomTypeException_Exception ex) {
                                            System.err.println("Invalid Room Type.");
                                            response = 1;
                                            break;
                                        } catch (InvalidRoomQuantityException_Exception ex) {
                                            System.err.println(ex.getMessage());
                                            response = 1;
                                                break;

                                        }
                                        System.out.println("");
                                        System.out.println("Reservation Successful!");
                                        System.out.println("");

                                        System.out.println("Same day check-in (and aftter 2am)?");
                                        System.out.println("1: Yes");
                                        System.out.println("2: No");
                                        response = scanner.nextInt();
                                        scanner.nextLine();
                                        if (response == 1) {
                                            service.getReservationWebServicePort().sameDayCheckIn(checkInDate);
                                        }
                                        response = 0;
                                    }

                                }
                            }

                        } catch (ParseException_Exception | InvalidLoginCredentialException_Exception ex) {
                            System.err.println(ex.getMessage());
                            break;
                        }

                    } else if (response == 3) {
                        if (currentCustomer == null) {
                            System.err.println("Please Login To Use this feature.");
                            break;
                        }
                        try {
                            List<Reservations> reservations = service.getReservationWebServicePort().viewAllReservations(currentCustomer.getPassportNum(), currentCustomer.getPassword());
                            System.out.printf("%14s%26s\n", "Reservation ID", "Date");

                            for (Reservations r : reservations) {
                                Date startDate = r.getStartDate().toGregorianCalendar().getTime();
                                Date endDate = r.getEndDate().toGregorianCalendar().getTime();
                                //System.out.println(r.getReservationID() +  " " + outputDateFormat.format(r.getStartDate()) + "-" + outputDateFormat.format(r.getEndDate()));
                                System.out.printf("%14s%26s\n", r.getReservationID().toString(), outputDateFormat.format(startDate) + "-" + outputDateFormat.format(endDate));

                            }
                        } catch (InvalidLoginCredentialException_Exception ex) {
                            System.err.println(ex.getMessage());
                        }

                    } else if (response == 4) {
                        if (currentCustomer == null) {
                            System.err.println("Please Login To Use this feature.");
                            break;
                        }
                        System.out.print("Please enter reservation ID> ");
                        Long reservationID = scanner.nextLong();
                        scanner.nextLine();
                        try {
                            Reservations r = service.getReservationWebServicePort().viewReservation(currentCustomer.getPassportNum(), currentCustomer.getPassword(), reservationID);
                                     Date startDate = r.getStartDate().toGregorianCalendar().getTime();
                                Date endDate = r.getEndDate().toGregorianCalendar().getTime();
                            System.out.printf("%14s%35s      %s%26s\n", "Reservation ID", "Room Type", "Cost", "Date");
                            //System.out.printf("%14s%16s      %s\n", r.getReservationID().toString(), r.getReservationRoomType().getRoomTypeName(), r.getCost());
                            System.out.printf("%14s%35s       %s%26s\n", r.getReservationID().toString(), r.getReservationRoomType().getRoomTypeName(), r.getCost(), outputDateFormat.format(startDate) + "-" + outputDateFormat.format(endDate));

                        } catch (InvalidLoginCredentialException_Exception | ReservationNotFoundException_Exception ex) {
                            System.err.println(ex.getMessage());
                        }

                    }

                } catch (InputMismatchException ex) {
                    System.err.println("Input type mismatch.");
                    scanner.nextLine();
                    break;
                } catch (InvalidLoginCredentialException_Exception ex) {
                    System.err.println("Invalid Login Credentials");
                    System.out.println();
                } 
            }
            if (response == 5) {
                break;
            }
        }

    }

}
