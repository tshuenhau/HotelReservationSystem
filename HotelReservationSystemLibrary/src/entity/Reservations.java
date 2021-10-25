/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author tshuenhau
 */
@Entity
public class Reservations implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationID;
    
    @ManyToOne(optional = true)
    private Customers reservedBy;
  
    @Column(columnDefinition = "VARCHAR(60) CHECK (roomType IN ('Deluxe Room', 'Premier Room', 'Family Room', 'Junior Suite', 'Grand Suite'))")
    private String roomType;
  
    @Column(nullable=false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;

    @Column(nullable=false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endDate;

    @Column(nullable=false)
    private Boolean isAllocated = false;

    @Column(nullable=true)
    private Integer allocatedRoom;

    public Reservations() {
    }

    public Reservations(Customers reservedBy, String roomType, Date startDate, Date endDate) {
        this.reservedBy = reservedBy;
        this.roomType = roomType;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    public Long getReservationID() {
        return reservationID;
    }

    public void setReservationID(Long reservationID) {
        this.reservationID = reservationID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationID != null ? reservationID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reservationID fields are not set
        if (!(object instanceof Reservations)) {
            return false;
        }
        Reservations other = (Reservations) object;
        if ((this.reservationID == null && other.reservationID != null) || (this.reservationID != null && !this.reservationID.equals(other.reservationID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Reservations[ id=" + reservationID + " ]";
    }

    /**
     * @return the reservedBy
     */
    public Customers getReservedBy() {
        return reservedBy;
    }

    /**
     * @param reservedBy the reservedBy to set
     */
    public void setReservedBy(Customers reservedBy) {
        this.reservedBy = reservedBy;
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
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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

    /**
     * @return the allocatedRoom
     */
    public Integer getAllocatedRoom() {
        return allocatedRoom;
    }

    /**
     * @param allocatedRoom the allocatedRoom to set
     */
    public void setAllocatedRoom(Integer allocatedRoom) {
        this.allocatedRoom = allocatedRoom;
    }
    
}
