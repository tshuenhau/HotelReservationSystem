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
public class InvalidRoomTypeException extends Exception {
    public InvalidRoomTypeException(){
    }
    
    public InvalidRoomTypeException(String msg){
        super(msg);
    }
    
}
