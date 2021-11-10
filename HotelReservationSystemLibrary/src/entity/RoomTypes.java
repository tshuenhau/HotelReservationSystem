/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
    
    @JoinColumn(nullable = true)
    private RoomTypes nextHigherRoomType;
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    private Integer size;
    
    @Column(nullable = false)
    private String bed;
    
    @Column(nullable = false)
    private Integer capacity;
    
    @Column(nullable = false)
    private String amenities;
    
    @OneToMany(mappedBy = "rmType")
    private List<HotelRooms> hotelRooms;
    
    @OneToMany(mappedBy = "roomType")
    private List<Rates> roomRates;
    
    @OneToOne()
    private Reservations reservation;

    public RoomTypes() {
        hotelRooms = new ArrayList<>();
        roomRates = new ArrayList<>();
        reservation = null;
    }

    public RoomTypes(String roomTypeName, RoomTypes nextHigherRoomType) {
        this();
        this.roomTypeName = roomTypeName;
        this.nextHigherRoomType = nextHigherRoomType;
        this.amenities = "Pool";
        this.bed = "King";
        this.capacity = 10;
        this.description = "good";
        this.size = 2;
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
    public RoomTypes getNextHigherRoomType() {
        return nextHigherRoomType;
    }

    /**
     * @param nextHigherRoomType the nextHigherRoomType to set
     */
    public void setNextHigherRoomType(RoomTypes nextHigherRoomType) {
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
    public Integer getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(Integer size) {
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
    public Integer getCapacity() {
        return capacity;
    }

    /**
     * @param capacity the capacity to set
     */
    public void setCapacity(Integer capacity) {
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
    public List<HotelRooms> getHotelRoom() {
        return hotelRooms;
    }

    /**
     * @param hotelRoom the hotelRoom to set
     */
    public void setHotelRoom(List<HotelRooms> hotelRoom) {
        this.hotelRooms = hotelRoom;
    }

    /**
     * @return the roomRate
     */
    public List<Rates> getRoomRate() {
        return roomRates;
    }

    /**
     * @param roomRate the roomRate to set
     */
    public void setRoomRate(List<Rates> roomRate) {
        this.roomRates = roomRate;
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
