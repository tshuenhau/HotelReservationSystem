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
public class InvalidRoomQuantityException extends Exception{

    public InvalidRoomQuantityException() {
    }

    public InvalidRoomQuantityException(String msg) {
        super(msg);
    }
    
    
}
