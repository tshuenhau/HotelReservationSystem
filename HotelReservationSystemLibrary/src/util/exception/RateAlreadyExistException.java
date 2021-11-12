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
public class RateAlreadyExistException extends Exception {
    
    public RateAlreadyExistException(String string) {
        super(string);
    }

    public RateAlreadyExistException() {
    }
}
