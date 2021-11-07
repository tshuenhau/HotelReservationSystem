/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import classes.Allocation;
import ejb.session.stateless.HotelRoomsEntitySessionBeanLocal;
import ejb.session.stateless.ReservationsEntitySessionBeanLocal;
import entity.HotelRooms;
import entity.Reservations;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import util.exception.AllocationException;

/**
 *
 * @author tshuenhau
 */
@Stateless
public class AllocationSessionBean implements AllocationSessionBeanRemote, AllocationSessionBeanLocal {

    @EJB
    private HotelRoomsEntitySessionBeanLocal hotelRoomsEntitySessionBeanLocal;
    SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");

    @EJB
    private ReservationsEntitySessionBeanLocal reservationsEntitySessionBeanLocal;

    List<AllocationException> allocationExceptions = new ArrayList<>();

    public List<AllocationException> viewAllocationException() {
        return allocationExceptions;
    }

    @Override
    public List<Allocation> allocateRooms(Date date) {
        //before allocating rooms;
        //iterate through all reservations and find r.endDate() = date and set hotel room allocated isAllocated= false;
        //
        // roomtype - number of reservations - inventory of unallocated 
        List<Allocation> allocationReport = new ArrayList<>();
        List<HotelRooms> hotelRooms = hotelRoomsEntitySessionBeanLocal.retrieveAllHotelRooms();
        List<Reservations> reservations = reservationsEntitySessionBeanLocal.retrieveAllReservations();
        allocationReport.add(new Allocation("Deluxe Room"));
        allocationReport.add(new Allocation("Premier Room"));
        allocationReport.add(new Allocation("Family Room"));
        allocationReport.add(new Allocation("Junior Suite"));
        allocationReport.add(new Allocation("Grand Suite"));

        for (Allocation a : allocationReport) {
            for (Reservations r : reservations) {
                if(r.getEndDate().equals(date)){
                    if(r.getAllocatedRoom() != null){
                        r.getAllocatedRoom().setIsAllocated(false);
                        reservationsEntitySessionBeanLocal.updateReservation(r);
                        r.setAllocatedRoom(null);
                    }
                    //r.setAllocatedRoom(null);
                    //need to set the hotel room allocated to this reservation to isAllocated = false;
                }
                //System.out.println(isWithinRange(date, r.getStartDate(), r.getEndDate()));
                if (date.equals(r.getStartDate()) && r.getRoomType().equals(a.getRoomType()) && r.getAllocatedRoom() == null) {
                    a.setNumReservations(a.getNumReservations() + 1);
                }
            }

            for (HotelRooms h : hotelRooms) {
                if (h.getRmType().equals(a.getRoomType()) && h.getStatus() == true && h.getIsAllocated() == false) {
                    a.setNumAvailable(a.getNumAvailable() + 1);
                }
            }
            System.out.println(a.getRoomType() + " " + a.getNumReservations() + " " + a.getNumAvailable());
        }

        for (int i = 0; i < allocationReport.size(); i++) {
            if(i == allocationReport.size()-1){ // if Grand suite
                if(allocationReport.get(i).canFulfil() == false){
                    int shortage = allocationReport.get(i).numShortage();
                    while(shortage > 0){
                        allocationExceptions.add(new AllocationException(allocationReport.get(i).getRoomType(), 2));
                        shortage -= 1;
                    }
                }
            }
            else if (allocationReport.get(i).canFulfil() == false) {
                Integer count = allocationReport.get(i).numShortage();
                System.out.println(count);

                if (allocationReport.get(i + 1).canFulfil() == true) { // if next tier can fulfil
                    Integer numUpgrades = -allocationReport.get(i + 1).numShortage();//number of upgrades possible
                        System.out.println(numUpgrades);

                    if (numUpgrades > 0) {
                        allocationReport.get(i + 1).setNumReservations(allocationReport.get(i + 1).getNumReservations() + numUpgrades);
                        allocationReport.get(i).setNumReservations(allocationReport.get(i + 1).getNumReservations() - numUpgrades);
                        Integer numType2 = count - numUpgrades;
                        while (numUpgrades > 0) {
                            allocationExceptions.add(new AllocationException(allocationReport.get(i).getRoomType(), 1));
                            numUpgrades -= 1;
                        }
                        while (numType2 > 0) {
                            allocationExceptions.add(new AllocationException(allocationReport.get(i).getRoomType(), 2));
                            numType2 -= 1;
                        }
                    }

                }
            }
        }
        
        
        for(Allocation a: allocationReport){
      
            System.out.println(a.getNumReservations() + " " + a.getNumAvailable() );            
        }
        
        for(AllocationException a: allocationExceptions){
              for(Reservations r: reservations){
                if(a.getExceptionType()== 1 && r.getStartDate().equals(date) && a.getRoomType().equals(r.getRoomType())){
                    if(a.getRoomType().equals("Deluxe Room")){
                        r.setRoomType("Premier Room");
                    }
                    else if(a.getRoomType().equals("Premier Room")){
                        r.setRoomType("Family Room");
                    }
                    else if(a.getRoomType().equals("Family Room")){
                        r.setRoomType("Junior Suite");
                    }
                    else if(a.getRoomType().equals("Junior Suite")){
                        r.setRoomType("Grand Suite");
                    }
                    reservationsEntitySessionBeanLocal.updateReservation(r);
                    break;
                }
            }
        }
        
        for(Reservations r: reservations){
            if(r.getStartDate().equals(date)){
                for(HotelRooms h: hotelRooms){
                    if(r.getRoomType().equals(h.getRmType()) && h.getIsAllocated() == false){ // not allocating those updates
                        System.out.println("ALLOCTE");
                        h.setIsAllocated(true);
                        r.setAllocatedRoom(h);
                        reservationsEntitySessionBeanLocal.updateReservation(r);
                        break;
                    }
            }
                
            }
        }
        
        
            System.out.println("EXCEPTION REPORT");
        for(AllocationException a: allocationExceptions){
            System.out.println(a.getExceptionType() + " " + a.getRoomType());
        }

        return allocationReport;

    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public boolean isWithinRange(Date date, Date startDate, Date endDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.add(calendar.DATE, -1);
        Date yesterday = calendar.getTime();
        return !(date.before(startDate) || date.after(yesterday));
    }
}
