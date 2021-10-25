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
import javax.persistence.OneToMany;

/**
 *
 * @author chenx
 */
@Entity
public class Customers implements Serializable {


    private static final long serialVersionUID = 1L;
    @Id
    private Long passportNum;
    
    @Column(nullable = false)
    private String password;
    
    @OneToMany(mappedBy = "customerID")
    private Long reservationID;

    public Customers() {
    }

    public Customers(Long passportNum, String password, Long reservationID) {
        this.passportNum = passportNum;
        this.password = password;
        this.reservationID = reservationID;
    }

    
    public Long getPassportNum() {
        return passportNum;
    }

    public void setPassportNum(Long passportNum) {
        this.passportNum = passportNum;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (passportNum != null ? passportNum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the passportNum fields are not set
        if (!(object instanceof Customers)) {
            return false;
        }
        Customers other = (Customers) object;
        if ((this.passportNum == null && other.passportNum != null) || (this.passportNum != null && !this.passportNum.equals(other.passportNum))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Customer[ id=" + passportNum + " ]";
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    
    /**
     * @return the reservationID
     */
    public Long getReservationID() {
        return reservationID;
    }

    /**
     * @param reservationID the reservationID to set
     */
    public void setReservationID(Long reservationID) {
        this.reservationID = reservationID;
    }
}
