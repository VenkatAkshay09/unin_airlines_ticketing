package com.unin.airlines.user_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.web.servlet.function.ServerResponse.status;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<String> handleExistingUserException(Exception ex) throws UserExistsException {
        return ResponseEntity.status(400).body(ex.getMessage());
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<String> handleDuplicateEmailException(Exception ex) throws UserExistsException {
        return ResponseEntity.status(400).body(ex.getMessage());
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) throws Exception{
        return ResponseEntity.status(400).body(ex.getMessage());
    }
}
