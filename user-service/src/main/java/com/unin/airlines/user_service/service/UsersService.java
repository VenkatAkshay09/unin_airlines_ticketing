package com.unin.airlines.user_service.service;

import com.unin.airlines.user_service.dto.UserRequestDto;
import com.unin.airlines.user_service.dto.UserResponseDto;
import com.unin.airlines.user_service.exception.UserExistsException;
import com.unin.airlines.user_service.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface UsersService {

    public UserResponseDto createUserRecord(UserRequestDto user) throws UserExistsException;

//    public boolean updatePwd(Long Id, String pwd);
//
//    public boolean deleteUserRecord(Long id);
//
//    public UserResponseDto upsertUserRecord(UserRequestDto user);
//
//    public UserResponseDto fetchUserRecord(Long id);
//
//    public List<UserResponseDto> fetchAllUsers();
}
