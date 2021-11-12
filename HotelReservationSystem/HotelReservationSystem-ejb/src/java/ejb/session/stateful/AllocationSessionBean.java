/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import classes.Allocation;
import ejb.session.stateless.HotelRoomsEntitySessionBeanLocal;
import ejb.session.stateless.ReservationsEntitySessionBeanLocal;
import ejb.session.stateless.RoomTypesEntitySessionBeanLocal;
import entity.HotelRooms;
import entity.Reservations;
import entity.RoomTypes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Schedules;
import javax.ejb.Stateless;
import util.exception.AllocationException;

/**
 *
 * @author tshuenhau
 */
@Stateless
public class AllocationSessionBean implements AllocationSessionBeanRemote, AllocationSessionBeanLocal {

    @EJB
    private RoomTypesEntitySessionBeanLocal roomTypesEntitySessionBeanLocal;

    @EJB
    private HotelRoomsEntitySessionBeanLocal hotelRoomsEntitySessionBeanLocal;
    SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");

    @EJB
    private ReservationsEntitySessionBeanLocal reservationsEntitySessionBeanLocal;
    List<Allocation> allocationReport = new ArrayList<>();
    List<HotelRooms> hotelRooms = new ArrayList<>();
    List<Reservations> reservations = new ArrayList<>();
    List<RoomTypes> roomTypes = new ArrayList<>();
    List<AllocationException> allocationExceptions = new ArrayList<>();

    @Schedules({
        @Schedule(dayOfWeek="*"),
        @Schedule(hour="2")
    })
    private void autoAllocate() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        allocateRooms(today.getTime());
    }

    public List<AllocationException> viewAllocationException(Date date) {

        return allocationExceptions;
    }

    @Override
    public List<Allocation> generateReport(Date date) {
        // roomtype - number of reservations - inventory of unallocated 
        hotelRooms = hotelRoomsEntitySessionBeanLocal.retrieveAllHotelRooms();
        reservations = reservationsEntitySessionBeanLocal.retrieveAllReservations();
        roomTypes = roomTypesEntitySessionBeanLocal.retrieveAllRoomTypes();
        for (RoomTypes r : roomTypes) {
            allocationReport.add(new Allocation(r));
        }

        for (Allocation a : allocationReport) {
            for (Reservations r : reservations) {
                if (isWithinRange(date, r.getStartDate(), r.getEndDate()) == false) {
                    if (r.getAllocatedRoom() != null) {
                        r.getAllocatedRoom().setIsAllocated(false);
                        //r.setAllocatedRoom(null);

                        reservationsEntitySessionBeanLocal.updateReservation(r);
                    }
                } 
                else if(isWithinRange(date, r.getStartDate(), r.getEndDate()) == true){
                    if (r.getAllocatedRoom() != null) {
                        r.getAllocatedRoom().setIsAllocated(true);
                        reservationsEntitySessionBeanLocal.updateReservation(r);
                    }
                }
                
                if (r.getStartDate().equals(date) && r.getReservationRoomType().equals(a.getRoomType()) && r.getAllocatedRoom() == null) {
                    a.setNumReservations(a.getNumReservations() + 1);
                }
                
            }

            for (HotelRooms h : hotelRooms) {
                if (h.getRmType().equals(a.getRoomType()) && h.getStatus() == true && h.getIsAllocated() == false) {
                    a.setNumAvailable(a.getNumAvailable() + 1);
                }
            }

            System.out.println("AVAILABILITY: " + a.getRoomType() + " " + a.getNumReservations() + " " + a.getNumAvailable());
        }
//next we generate the exceptions. i.e find the upgrades and stuff. need redo this part
        for (int i = 0; i < allocationReport.size(); i++) {
            if (allocationReport.get(i).canFulfil() == false) {//current roomtype cannot allocate fully
                Integer count = allocationReport.get(i).numShortage();
                System.out.println("count: " + count);
                //find the next upgrade
                if (allocationReport.get(i).getRoomType().getNextHigherRoomType() != null) {
                    // there is an upgrade
                    for (Allocation al : allocationReport) {
                        if (al.getRoomType().equals(allocationReport.get(i).getRoomType().getNextHigherRoomType()) && al.canFulfil() == true) {
                            Integer numUpgrades = -al.numShortage();//number of upgrades possible
                            System.out.println("numUpgrades: " + numUpgrades);
                            if (numUpgrades > 0) {
                                al.setNumReservations(al.getNumReservations() + numUpgrades);
                                allocationReport.get(i).setNumReservations(al.getNumReservations() - numUpgrades);
                                Integer numType2 = count - numUpgrades;
                                while (numUpgrades > 0 && count > 0) {
                                    allocationExceptions.add(new AllocationException(allocationReport.get(i).getRoomType(), 1));
                                    numUpgrades -= 1;
                                    count -= 1;
                                }
                                while (numType2 > 0) {
                                    allocationExceptions.add(new AllocationException(allocationReport.get(i).getRoomType(), 2));
                                    numType2 -= 1;
                                }
                            }
                        }
                    }
                } else {
                    //no upgradeÏ
                    Integer type2Count = allocationReport.get(i).numShortage();
                    while (type2Count > 0) {
                        allocationExceptions.add(new AllocationException(allocationReport.get(i).getRoomType(), 2));
                        type2Count -= 1;
                    }
                }

            }
        }

        for (Allocation a : allocationReport) {

            System.out.println(a.getNumReservations() + " " + a.getNumAvailable());
        }

        System.out.println("EXCEPTION REPORT");
        for (AllocationException a : allocationExceptions) {
            System.out.println(a.getExceptionType() + " " + a.getRoomType());
        }

        return allocationReport;

    }

    @Override
    public void allocateRooms(Date date) {
        generateReport(date);
        //this the part where we actually allocate
        for (AllocationException a : allocationExceptions) {
            for (Reservations r : reservations) {
                if (a.getExceptionType() == 1 && r.getStartDate().equals(date) && a.getRoomType().equals(r.getReservationRoomType())) {

                    r.setReservationRoomType(a.getRoomType().getNextHigherRoomType());

                    reservationsEntitySessionBeanLocal.updateReservation(r);
                    break;
                }
            }
        }

        for (Reservations r : reservations) {
            if (r.getStartDate().equals(date)) {
                for (HotelRooms h : hotelRooms) {
                    if (r.getReservationRoomType().equals(h.getRmType()) && h.getIsAllocated() == false && h.getStatus() == true && r.getAllocatedRoom() == null) { // not allocating those updates
                        System.out.println("ALLOCTE");
                        h.setIsAllocated(true);
                        r.setAllocatedRoom(h);
                        System.out.println("allocating reservation id:" + r.getReservationID());

                        reservationsEntitySessionBeanLocal.updateReservation(r);
                        break;
                    }
                }

            }
        }
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
