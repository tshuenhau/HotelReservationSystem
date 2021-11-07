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
public class AllocationException extends Exception {
    private Integer exceptionType;
    private String roomType;
    
    public AllocationException() {
    }

    public AllocationException(String roomType, Integer exceptionType) {
        super(roomType);
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
    public String getRoomType() {
        return roomType;
    }

    /**
     * @param roomType the roomType to set
     */
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
    
}
