/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

/**
 *
 * @author tshuenhau
 */
public class Allocation {
    
    private String roomType;
    private Integer numReservations;
    private Integer numAvailable;

    public Allocation() {
    }

    public Allocation(String roomType, Integer numReservations, Integer numAvailable) {
        this.roomType = roomType;
        this.numReservations = numReservations;
        this.numAvailable = numAvailable;
    }

    public Allocation(String roomType) {
        this.roomType = roomType;
        this.numReservations = 0;
        this.numAvailable = 0;
    }

    public Boolean canFulfil(){
        return numReservations <= numAvailable;
    }
    
    public Integer numShortage(){
        return numReservations - numAvailable;
    }
    
    /**
     * @return the roomType
     */
    public String getRoomType() {
        return roomType;
    }

    /**
     * @param roomType the roomType to set
     */
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    /**
     * @return the numReservations
     */
    public Integer getNumReservations() {
        return numReservations;
    }

    /**
     * @param numReservations the numReservations to set
     */
    public void setNumReservations(Integer numReservations) {
        this.numReservations = numReservations;
    }

    /**
     * @return the numAvailable
     */
    public Integer getNumAvailable() {
        return numAvailable;
    }

    /**
     * @param numAvailable the numAvailable to set
     */
    public void setNumAvailable(Integer numAvailable) {
        this.numAvailable = numAvailable;
    }
    
}
