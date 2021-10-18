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

/**
 *
 * @author chenx
 */
@Entity
public class Employees implements Serializable {


    private static final long serialVersionUID = 1L;
    @Id
    private String username;
    
    @Column(columnDefinition = "VARCHAR(60) CHECK (employeeType IN ('System Admin', 'Operations Manager', 'Sales Manager', 'Guest Relation Officer'))")
    private String employeeType;

    @Column(nullable=false)
    private String password;

    public Employees(String username, String employeeType, String password) {
        this.username = username;
        this.employeeType = employeeType;
        this.password = password;
    }

    public Employees() {
    }
    
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String id) {
        this.username = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (username != null ? username.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Employees)) {
            return false;
        }
        Employees other = (Employees) object;
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Employees[ username=" + username + " ]";
    }
    
    
    /**
     * @return the employeeType
     */
    public String getEmployeeType() {
        return employeeType;
    }

    /**
     * @param employeeType the employeeType to set
     */
    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
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
}
