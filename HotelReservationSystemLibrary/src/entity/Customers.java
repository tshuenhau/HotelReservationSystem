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
    private String password;
    
    @OneToMany(mappedBy="reservedBy")
    private List<Reservations> reservations = new ArrayList<Reservations>(); 

    public Customers(Long passportNum, String password) {
        this.passportNum = passportNum;
        this.password = password;
    }

    public Customers() {
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
        return "entity.Customers[ id=" + passportNum + " ]";
    }
    
}
