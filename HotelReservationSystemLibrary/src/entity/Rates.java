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
import javax.persistence.Temporal;

/**
 *
 * @author tshuenhau
 */
@Entity
public class Rates implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rateID;
    
    @Column(nullable = false, columnDefinition = "VARCHAR(60) CHECK (roomType IN ('Deluxe Room', 'Premier Room', 'Family Room', 'Junior Suite', 'Grand Suite'))")
    private String roomType;
    
    @Column(nullable = false, columnDefinition = "VARCHAR(60) CHECK (rateType IN ('Published', 'Normal', 'Peak', 'Promo'))")
    private String rateType;
    
    @Column(nullable = false)
    private Integer price;
    
    @Column(nullable = true)
    @Temporal(javax.persistence.TemporalType.DATE)    
    private Date startDate=null;
    
    @Column(nullable = true)
    @Temporal(javax.persistence.TemporalType.DATE)    
    private Date endDate=null;

    public Rates(String roomType, String rateType, Integer price) {
        this.roomType = roomType;
        this.rateType = rateType;
        this.price = price;
    }

    public Rates(Long rateID, String roomType, String rateType, Integer price) {
        this.rateID = rateID;
        this.roomType = roomType;
        this.rateType = rateType;
        this.price = price;
    }
    
    public Rates() {
    }
    public Long getRateID() {
        return rateID;
    }

    public void setRateID(Long rateID) {
        this.rateID = rateID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rateID != null ? rateID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rateID fields are not set
        if (!(object instanceof Rates)) {
            return false;
        }
        Rates other = (Rates) object;
        if ((this.rateID == null && other.rateID != null) || (this.rateID != null && !this.rateID.equals(other.rateID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Rates[ id=" + rateID + " ]";
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
     * @return the rateType
     */
    public String getRateType() {
        return rateType;
    }

    /**
     * @param rateType the rateType to set
     */
    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    /**
     * @return the price
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(Integer price) {
        this.price = price;
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
    
}
