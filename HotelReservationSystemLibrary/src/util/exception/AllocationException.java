/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

import entity.RoomTypes;

/**
 *
 * @author tshuenhau
 */
public class AllocationException extends Exception {
    private Integer exceptionType;
    private RoomTypes roomType;
    
    public AllocationException() {
    }

    public AllocationException(RoomTypes roomType, Integer exceptionType) {
        this.roomType = roomType;
        this.exceptionType = exceptionType;
        
    }

    /**
     * @return the exceptionType
     */
    public Integer getExceptionType() {
        return exceptionType;
    }

    /**
     * @param exceptionType the exceptionType to set
     */
    public void setExceptionType(Integer exceptionType) {
        this.exceptionType = exceptionType;
    }

    /**
     * @return the roomType
     */
    public RoomTypes getRoomType() {
        return roomType;
    }

    /**
     * @param roomType the roomType to set
     */
    public void setRoomType(RoomTypes roomType) {
        this.roomType = roomType;
    }
    
}
