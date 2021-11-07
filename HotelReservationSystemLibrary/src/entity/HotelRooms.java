/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author tshuenhau
 */
@Entity
public class HotelRooms implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hotelRoomID;
    
    @Column(nullable = false, columnDefinition = "VARCHAR(60) CHECK (rmType IN ('Deluxe Room', 'Premier Room', 'Family Room', 'Junior Suite', 'Grand Suite'))")
    private String rmType;
    
    @Column(nullable = false)
    private Boolean status = true;
    
    @Column(nullable = false)
    private Boolean isAllocated = false;
    
    @OneToOne(mappedBy="allocatedRoom")
    private Reservations reservation;

    public HotelRooms(String rmType) {
        this.rmType = rmType;
    }

    public HotelRooms() {
    }

    public Long getHotelRoomID() {
        return hotelRoomID;
    }

    public void setHotelRoomID(Long hotelRoomID) {
        this.hotelRoomID = hotelRoomID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hotelRoomID != null ? hotelRoomID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the hotelRoomID fields are not set
        if (!(object instanceof HotelRooms)) {
            return false;
        }
        HotelRooms other = (HotelRooms) object;
        if ((this.hotelRoomID == null && other.hotelRoomID != null) || (this.hotelRoomID != null && !this.hotelRoomID.equals(other.hotelRoomID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.HotelRooms[ id=" + hotelRoomID + " ]";
    }

    /**
     * @return the rmType
     */
    public String getRmType() {
        return rmType;
    }

    /**
     * @param rmType the rmType to set
     */
    public void setRmType(String rmType) {
        this.rmType = rmType;
    }

    /**
     * @return the status
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * @return the isAllocated
     */
    public Boolean getIsAllocated() {
        return isAllocated;
    }

    /**
     * @param isAllocated the isAllocated to set
     */
    public void setIsAllocated(Boolean isAllocated) {
        this.isAllocated = isAllocated;
    }
    
}
