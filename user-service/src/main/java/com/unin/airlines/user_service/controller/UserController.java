package com.unin.airlines.user_service.controller;

import com.unin.airlines.user_service.dto.UserRequestDto;
import com.unin.airlines.user_service.dto.UserResponseDto;
import com.unin.airlines.user_service.exception.DuplicateEmailException;
import com.unin.airlines.user_service.exception.UserExistsException;
import com.unin.airlines.user_service.service.UsersService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    UsersService usersService;

    @PostMapping("/create-user")
    public ResponseEntity<UserResponseDto> createUserRecord(@Valid @RequestBody UserRequestDto userRequestDto) throws UserExistsException {
//        String userEmail = userRequestDto.getEmail();
//        boolean dupEmail = usersService.fetchRecordByEmail(userEmail);
//        if(dupEmail){
//            log.error("email already exists {},{}",userEmail, LocalDateTime.now());
//            throw new DuplicateEmailException(userEmail+" An account already exists with the given mail");
//        }
//        try{
            UserResponseDto userResponseDto = usersService.createUserRecord(userRequestDto);
            log.info("user record created successfully- {} at -{}",userResponseDto,LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
//            }
//        catch (Exception ex){
//            log.error("Error creating user: {} at {}", ex.getMessage(), LocalDateTime.now());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }

    }
}
