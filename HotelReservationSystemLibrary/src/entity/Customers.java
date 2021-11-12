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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author tshuenhau
 */
@Entity
public class Customers implements Serializable {

    
    private static final long serialVersionUID = 1L;
    @Id
    private Long passportNum;
    
    @Column(nullable = false)
    private String name = "";
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private Boolean isPartner;
    
    @OneToMany(mappedBy="reservedBy")
    private List<Reservations> reservations = new ArrayList<Reservations>(); 
    
    
    public Customers() {
        reservations = new ArrayList<>();
    }
       

    public Customers(Long passportNum, String password, String name, Boolean isPartner) {
        this();
        this.passportNum = passportNum;
        this.password = password;
        this.isPartner = isPartner;
        this.name = name;
    }

    public Customers(Long passportNum, String name, String password) {
        this();
        this.passportNum = passportNum;
        this.password = password;
        this.name = name;
        this.isPartner = false;
    }

    public Long getPassportNum() {
        return passportNum;
    }

    public void setPassportNum(Long passportNum) {
        this.passportNum = passportNum;
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
        return "entity.Customers[ id=" + passportNum + " ]";
    }

    /**
     * @return the isPartner
     */
    public Boolean getIsPartner() {
        return isPartner;
    }

    /**
     * @param isPartner the isPartner to set
     */
    public void setIsPartner(Boolean isPartner) {
        this.isPartner = isPartner;
    }

    /**
     * @return the reservations
     */
    public List<Reservations> getReservations() {
        return reservations;
    }

    /**
     * @param reservations the reservations to set
     */
    public void setReservations(List<Reservations> reservations) {
        this.reservations = reservations;
    }
    
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
