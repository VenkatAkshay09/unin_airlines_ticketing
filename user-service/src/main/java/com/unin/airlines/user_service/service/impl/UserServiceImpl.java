package com.unin.airlines.user_service.service.impl;

import com.unin.airlines.user_service.dto.UserRequestDto;
import com.unin.airlines.user_service.dto.UserResponseDto;
import com.unin.airlines.user_service.entity.Users;
import com.unin.airlines.user_service.exception.UserExistsException;
import com.unin.airlines.user_service.repository.UserRepo;
import com.unin.airlines.user_service.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UsersService {

    @Autowired
    private UserRepo repo;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public UserResponseDto createUserRecord(UserRequestDto userDto) throws UserExistsException{
        UserResponseDto userResponseDto = new UserResponseDto();
        try{
            log.info("Attempting to create user: {}", userDto.getEmail());
            Optional<Users> emailRecord = repo.findByEmail(userDto.getEmail());
            Optional<Users> phRecord = repo.findByPhNumber(userDto.getPhNumber());
            if (emailRecord.isPresent()) {
                throw new UserExistsException("Email already exists: " + userDto.getEmail());
            }

            if (phRecord.isPresent()) {
                throw new UserExistsException("Phone number already exists: " + userDto.getPhNumber());
            }

            Users user = new Users();
            modelMapper.map(userDto, user);
            Users createdUserRecord = repo.save(user);
            modelMapper.map(createdUserRecord, userResponseDto);
            log.info("User created successfully with ID: {}", createdUserRecord.getUserId());

        }
        catch (UserExistsException ex) {
            log.warn("User creation blocked: {}", ex.getMessage());
            throw ex;
        }
        catch(Exception ex){
            log.error(ex.getMessage()+ userDto.getEmail() + "error occurred while creating record");
            throw ex;
        }
        return userResponseDto;
    }
}
