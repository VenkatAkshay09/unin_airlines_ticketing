package com.unin.airlines.user_service.service;

import com.unin.airlines.user_service.dto.*;
import com.unin.airlines.user_service.exception.InvalidCredentialsException;
import com.unin.airlines.user_service.exception.UserExistsException;
import com.unin.airlines.user_service.repository.UserRepo;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface UsersService {

    public UserResponseDto createUserRecord(UserRequestDto user) throws UserExistsException;

    public boolean fetchRecordByEmail(String email);

    public String generatePwd();

    public boolean updatePwd(FirstLoginRequestDto firstLoginRequestDto) throws Exception;

    LoginResponseDto validateLogin(@Valid LoginRequestDto loginRequestDto) throws InvalidCredentialsException;

//    public boolean deleteUserRecord(Long id);
//
    public UserResponseDto upsertUserRecord(UserResponseDto user);
//
//    public UserResponseDto fetchUserRecord(Long id);
//
//    public List<UserResponseDto> fetchAllUsers();
}
