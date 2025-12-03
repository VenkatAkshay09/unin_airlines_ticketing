package com.unin.airlines.user_service.exception;

public class DuplicateEmailException extends Exception{
    public DuplicateEmailException(String msg){
        super(msg);
    }
}
