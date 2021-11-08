/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author chenx
 */
@Entity
public class RoomTypes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String roomTypeName;
    
    @Column(nullable = false)
    private String nextHigherRoomType;
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    private String size;
    
    @Column(nullable = false)
    private String bed;
    
    @Column(nullable = false)
    private String capacity;
    
    @Column(nullable = false)
    private String amenities;
    
    @OneToOne
    private HotelRooms hotelRoom;
    
    @OneToOne
    private Rates roomRate;
    
    @OneToOne
    private Reservations reservation;

    public RoomTypes() {
    }

    public RoomTypes(String roomTypeName, String nextHigherRoomType) {
        this.roomTypeName = roomTypeName;
        this.nextHigherRoomType = nextHigherRoomType;
    }
    
    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomTypeName != null ? roomTypeName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomTypeName fields are not set
        if (!(object instanceof RoomTypes)) {
            return false;
        }
        RoomTypes other = (RoomTypes) object;
        if ((this.roomTypeName == null && other.roomTypeName != null) || (this.roomTypeName != null && !this.roomTypeName.equals(other.roomTypeName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomTypes[ id=" + roomTypeName + " ]";
    }

    /**
     * @return the nextHigherRoomType
     */
    public String getNextHigherRoomType() {
        return nextHigherRoomType;
    }

    /**
     * @param nextHigherRoomType the nextHigherRoomType to set
     */
    public void setNextHigherRoomType(String nextHigherRoomType) {
        this.nextHigherRoomType = nextHigherRoomType;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the size
     */
    public String getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * @return the bed
     */
    public String getBed() {
        return bed;
    }

    /**
     * @param bed the bed to set
     */
    public void setBed(String bed) {
        this.bed = bed;
    }

    /**
     * @return the capacity
     */
    public String getCapacity() {
        return capacity;
    }

    /**
     * @param capacity the capacity to set
     */
    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    /**
     * @return the amenities
     */
    public String getAmenities() {
        return amenities;
    }

    /**
     * @param amenities the amenities to set
     */
    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    /**
     * @return the hotelRoom
     */
    public HotelRooms getHotelRoom() {
        return hotelRoom;
    }

    /**
     * @param hotelRoom the hotelRoom to set
     */
    public void setHotelRoom(HotelRooms hotelRoom) {
        this.hotelRoom = hotelRoom;
    }

    /**
     * @return the roomRate
     */
    public Rates getRoomRate() {
        return roomRate;
    }

    /**
     * @param roomRate the roomRate to set
     */
    public void setRoomRate(Rates roomRate) {
        this.roomRate = roomRate;
    }

    /**
     * @return the reservation
     */
    public Reservations getReservation() {
        return reservation;
    }

    /**
     * @param reservation the reservation to set
     */
    public void setReservation(Reservations reservation) {
        this.reservation = reservation;
    }
    
}
