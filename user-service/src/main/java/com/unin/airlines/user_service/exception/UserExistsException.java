package com.unin.airlines.user_service.exception;

public class UserExistsException extends Exception{
    public UserExistsException(String msg){
        super(msg);
    }
}
