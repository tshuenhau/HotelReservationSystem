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
    
    @Column(nullable = false)
    private String rmType;
    
    @Column(nullable = false)
    private Boolean status;

    public HotelRooms(String rmType, Boolean status) {
        this.rmType = rmType;
        this.status = status;
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
    
}
