/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelreservationsystemclient;

import ejb.session.stateful.HotelReservationSessionBeanRemote;
import entity.Reservations;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import util.exception.InvalidRoomQuantityException;
import util.exception.InvalidRoomTypeException;

/**
 *
 * @author tshuenhau
 */
public class ReservationClient {
    
    private HotelReservationSessionBeanRemote hotelReservationSessionBeanRemote;

    

    public ReservationClient() {
    }

    public ReservationClient(HotelReservationSessionBeanRemote hotelReservationSessionBeanRemote) {
        this.hotelReservationSessionBeanRemote = hotelReservationSessionBeanRemote;
    }



    
    public void runApp(){
        Scanner scanner = new Scanner(System.in);

        Integer response = 0;

        while(true){
            System.out.println("*** Welcome to Holiday Reservation Client ***\n");
            
            response = 0;
            System.out.println("1: Login (NOT YET IMPLEMENTED)");
            System.out.println("2: Register (NOT YET IMPLEMENTED)");
            System.out.println("3: Search Hotel Room");
            System.out.println("4: Exit\n");

            while(response < 1 || response > 4){                
                System.out.print("> ");
                response = scanner.nextInt();
                if(response == 1 ){
                    //DO LOGIN STUFF
                }
                
                if(response == 2 ){
                    //DO REGISTER STUFF
                }
                else if (response == 3)
                {
                    doSearchRoom();
                }

            }
            
            if(response == 4){
                System.out.println("Exited Reservation Client\n");
                break;
            }
        }
    }
    
    private void doReserveRoom(){
        try{
        //MAYBE DO TYPE IN ROOM NAME + QUANTITY
            Scanner scanner = new Scanner(System.in);

            Integer response = 0;
            String roomType = "";
            List<Reservations> reservations = new ArrayList<>();
            Integer quantity = 0;
            
                System.out.println("*** Holiday Reservation System :: Reserve Hotel Room ***\n");
                while(response < 1 || response > 3){
                response = 1;
                while(response == 1 ){
                    System.out.print("Please enter hotel room type> ");
                    roomType = scanner.nextLine();
                    System.out.print("Please enter quantity> ");
                    quantity = scanner.nextInt();
                    scanner.nextLine();
                    reservations = hotelReservationSessionBeanRemote.addReservation(roomType, quantity);
                    System.out.println("1: Add more rooms");
                    System.out.println("2: Confirm");
                    System.out.println("3: Cancel");
                    response = 0;
                    while(response < 1 || response > 3){
                        System.out.print("> ");
                        response = scanner.nextInt();
                        scanner.nextLine();
                         if(response == 2){
                    System.out.println("Confirm the following reservations?\n");
                    if(reservations != null){
                        for(Reservations r: reservations){
                            System.out.println(r.getReservationID() + " " + r.getRoomType());
                        }
                    }
                    int reserveResponse = 0;
                    System.out.println("1: Confirm");
                    System.out.println("2: Cancel");
                    reserveResponse = scanner.nextInt();
                    if(reserveResponse == 1){
                        
                        hotelReservationSessionBeanRemote.confirmReservations();
                        System.out.println("Confirmed\n");

                    }
                    else if(reserveResponse == 2){
                        System.out.println("Cancelled\n");
                        break;
                    }
                }

                if(response == 3){
                    System.out.println("Exited Reservation Function\n");
                    break;
                }
                        
                    }

                }
               
            }
        }
        catch(InvalidRoomTypeException | InvalidRoomQuantityException ex){
            System.out.println(ex.getMessage());
            handleReservationExceptions();
        }

    }
    
    private void handleReservationExceptions(){
        //MAYBE DO TYPE IN ROOM NAME + QUANTITY
        Scanner scanner = new Scanner(System.in);

        Integer response = 0;
        System.out.println("1: Try Again");
        System.out.println("2: Exit");  
        while(response < 1 || response > 2){
            System.out.print("> ");
            response = scanner.nextInt();
            if(response == 1){
                doReserveRoom();
            }
        }

        

    }
    
    private void doSearchRoom(){
         try{
            Scanner scanner = new Scanner(System.in);
            Integer response = 0;
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");//"dd/MM/yyyy hh:mm a"
            Date checkInDate;
            Date checkOutDate;

            System.out.println("*** Holiday Reservation System :: Search Hotel Room ***\n");
            
            System.out.print("Enter Check-In Date (dd/mm/yyyy)> ");
            checkInDate = inputDateFormat.parse(scanner.nextLine().trim());
            System.out.print("Enter Check-Out Date (dd/mm/yyyy)> ");
            checkOutDate = inputDateFormat.parse(scanner.nextLine().trim()); 
            
            System.out.println("*** Showing available rooms for: " + outputDateFormat.format(checkInDate) + " to " + outputDateFormat.format(checkOutDate) + "***\n");
            
            System.out.printf("%8s%22s   %s\n", "Room Type", "Availability ", "Price");
            Map<String, List<Integer>> availability = hotelReservationSessionBeanRemote.searchHotelRooms(checkInDate,checkOutDate);
            
            availability.entrySet().forEach(rooms -> {
                System.out.printf("%8s%15s           %s\n", rooms.getKey() , rooms.getValue().get(0), rooms.getValue().get(1));

                //System.out.println(rooms.getKey() + "      " + rooms.getValue().get(0) + "    " + rooms.getValue().get(1));
             });
            System.out.println("Reserve Room?\n");
            System.out.println("1: Reserve Room");
            System.out.println("2: Exit");
            while(response < 1 || response > 2){
                System.out.print("> ");
                response = scanner.nextInt();
                if(response == 1){
                    doReserveRoom();
                }
            }
 
        }
        catch(ParseException ex)
        {
            System.out.println("Invalid date input!\n");
        }

    }

    private HotelReservationSessionBeanRemote lookupHotelReservationSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (HotelReservationSessionBeanRemote) c.lookup("java:comp/env/HotelReservationSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}

    
    /*
    
private void doSearchHoliday()
    {
        try
        {
            Scanner scanner = new Scanner(System.in);
            Integer response = 0;
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
            Date departureDate;
            Date returnDate;
            String departureCity;
            String destinationCity;
            Integer numberOfTravellers;

            System.out.println("*** Holiday Reservation System :: Search Holiday ***\n");
            System.out.print("Enter Departure Date (dd/mm/yyyy)> ");
            departureDate = inputDateFormat.parse(scanner.nextLine().trim());
            System.out.print("Enter Return Date (dd/mm/yyyy)> ");
            returnDate = inputDateFormat.parse(scanner.nextLine().trim());            
            System.out.print("Enter Departure City> ");
            departureCity = scanner.nextLine().trim();
            System.out.print("Enter Destination City> ");
            destinationCity = scanner.nextLine().trim();
            System.out.print("Enter Number of Travellers> ");
            numberOfTravellers = scanner.nextInt();
            
            List<ItineraryItem> itineraryItems = holidayReservationSessionBeanRemote.searchHolidays(departureDate, returnDate, departureCity, destinationCity, numberOfTravellers);
            
            System.out.printf("%8s%22s   %s\n", "Seq. No.", "Date/Time", "Itinerary");
            
            for(ItineraryItem itineraryItem:itineraryItems)
            {
                System.out.printf("%8s%22s   %s\n", itineraryItem.getSequenceNumber(), outputDateFormat.format(itineraryItem.getDateTime()), itineraryItem.getActivity());
            }
            
            System.out.println("------------------------");
            System.out.println("1: Make Reservation");
            System.out.println("2: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();
            
            if(response == 1)
            {
                if(currentCustomer != null)
                {
                    PaymentModeEnum paymentMode;
                    String creditCardNumber;
                    
                    System.out.println("\nTotal Amount Payable is " + BigDecimalHelper.formatCurrency(holidayReservationSessionBeanRemote.getTotalAmount()));
                    
                    while(true)
                    {
                        System.out.print("Select Payment Mode (1: VISA, 2: MasterCard, 3: AMEX)> ");
                        Integer paymentModeInt = scanner.nextInt();

                        if(paymentModeInt >= 1 && paymentModeInt <= 3)
                        {
                            paymentMode = PaymentModeEnum.values()[paymentModeInt-1];
                            break;
                        }
                        else
                        {
                            System.out.println("Invalid option, please try again!\n");
                        }
                    }
                    
                    scanner.nextLine();
                    System.out.print("Enter Credit Card Number> ");
                    creditCardNumber = scanner.nextLine().trim();
                    
                    try 
                    {
                        Long newTransactionId = holidayReservationSessionBeanRemote.reserveHoliday(currentCustomer.getCustomerId(), paymentMode, creditCardNumber);
                        
                        System.out.println("Reservation of holiday completed successfully!: " + newTransactionId + "\n");
                    } 
                    catch (CheckoutException ex) 
                    {
                        System.out.println("An error has occurred while making the reservation: " + ex.getMessage() + "\n");
                    }
                }
                else
                {
                    System.out.println("Please login first before making a reservation!\n");
                }
            }
        }
        catch(ParseException ex)
        {
            System.out.println("Invalid date input!\n");
        }
    }
    */
