/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author chenx
 */
public class RoomNotFoundException extends Exception {
    
    public RoomNotFoundException(String string) {
        super(string);
    }

    public RoomNotFoundException() {
    }
}
