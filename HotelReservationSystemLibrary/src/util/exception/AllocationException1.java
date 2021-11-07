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
public class AllocationException1 extends Exception {
    String roomType;

    public AllocationException1() {
    }

    public AllocationException1(String string) {
        super(string);
        this.roomType = string;
    }
    
}
