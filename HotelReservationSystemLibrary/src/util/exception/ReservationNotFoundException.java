/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author tshuenhau
 */
public class ReservationNotFoundException extends Exception {

    public ReservationNotFoundException(String string) {
        super(string);
    }

    public ReservationNotFoundException() {
    }
    
    
}
