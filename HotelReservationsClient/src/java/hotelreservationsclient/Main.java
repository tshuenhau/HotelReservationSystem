/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelreservationsclient;

import ejb.session.stateful.AllocationSessionBeanRemote;
import ejb.session.stateful.HotelReservationSessionBeanRemote;
import ejb.session.stateless.CustomersEntitySessionBeanRemote;
import ejb.session.stateless.HotelRoomsEntitySessionBeanRemote;
import javax.ejb.EJB;
import ejb.session.stateless.EmployeesEntitySessionBeanRemote;
import ejb.session.stateless.RatesEntitySessionBeanRemote;
import ejb.session.stateless.ReservationsEntitySessionBeanRemote;
import ejb.session.stateless.RoomTypesEntitySessionBeanRemote;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author chenx
 */
public class Main {

    @EJB
    private static AllocationSessionBeanRemote allocationSessionBeanRemote;

    @EJB
    private static RoomTypesEntitySessionBeanRemote roomTypesEntitySessionBeanRemote;

    @EJB
    private static HotelReservationSessionBeanRemote hotelReservationSessionBeanRemote;

    @EJB
    private static ReservationsEntitySessionBeanRemote reservationsEntitySessionBeanRemote;

    @EJB
    private static CustomersEntitySessionBeanRemote customersEntitySessionBeanRemote;

    @EJB
    private static RatesEntitySessionBeanRemote ratesEntitySessionBeanRemote;

    @EJB
    private static HotelRoomsEntitySessionBeanRemote hotelRoomsEntitySessionBeanRemote;

    @EJB
    private static EmployeesEntitySessionBeanRemote employeeEntitySessionBeanRemote;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ReservationClient reservationClient = new ReservationClient(hotelReservationSessionBeanRemote, customersEntitySessionBeanRemote, reservationsEntitySessionBeanRemote, allocationSessionBeanRemote);

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to Holiday Reservation System ***\n");

            System.out.println("1: Reservation Client");
            System.out.println("2: Exit\n");

            response = 0;

            while (response < 1 || response > 2) {
                try {
                    System.out.print("> ");
                    response = scanner.nextInt();
                    if (response == 1) {
                        reservationClient.runApp();

                    } else if (response == 2) {
                        break;

                    } else {
                        System.out.println("Invalid option, please try again!\n");
                    }

                    if (response == 3) {
                        System.out.println("Thanks for using our services!");
                        break;
                    }
                } catch (InputMismatchException ex) {
                    System.err.println("\nInput type mismatch.\n");
                    scanner.nextLine();
                    break;

                }
            }
            if (response == 3) {
                break;
            }
        }
    }
}
