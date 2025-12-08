package com.unin.airlines.user_service.service.impl;

import com.unin.airlines.user_service.config.JwtUtil;
import com.unin.airlines.user_service.dto.*;
import com.unin.airlines.user_service.entity.Users;
import com.unin.airlines.user_service.exception.InvalidCredentialsException;
import com.unin.airlines.user_service.exception.UserExistsException;
import com.unin.airlines.user_service.repository.UserRepo;
import com.unin.airlines.user_service.service.MailService;
import com.unin.airlines.user_service.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UsersService {


    private final UserRepo repo;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final JwtUtil jwtUtil;

//    public UserServiceImpl(UserRepo repo, ModelMapper modelMapper) {
//        this.repo = repo;
//        this.modelMapper = modelMapper;
//    }

    private static final String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String lowerCase = "abcdefghijklmnopqrstuvwxyz";
    private static final String numericCase = "1234567890";
    private static final String specialCase = "~!@#$%^&*()_-+=.<>?";
    private static final String allCharCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890~!@#$%^&*()_-+=.<>?";


    @Override
    public UserResponseDto createUserRecord(UserRequestDto userDto) throws UserExistsException{
//        UserResponseDto userResponseDto = new UserResponseDto();
//        try{
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
//            user.setPassword(generatePwd());
            String rawPwd = generatePwd();
            String hashedPwd = passwordEncoder.encode(rawPwd);
            user.setPassword(hashedPwd);
            Users createdUserRecord = repo.save(user);
            String message = String.format("Thaknyou for creating a account in Unin Airlines \n Your login password is %s \n Please update your password immediately", rawPwd);
            mailService.sendSimpleMail(user.getEmail(),"First login credentials", message);
            log.info("User created successfully with ID: {}", createdUserRecord.getUserId());
            return modelMapper.map(createdUserRecord, UserResponseDto.class);

//        }
//        catch (UserExistsException ex) {
//            log.error("User creation blocked: {}", ex.getMessage());
//            throw ex;
//        }
//        catch(Exception ex){
//            log.error("Error creating user for email {} : {}", userDto.getEmail(), ex.getMessage());
//            return new UserResponseDto();
////            throw ex;
//        }
//        return userResponseDto;
    }

    @Override
    public boolean fetchRecordByEmail(String email) {
        Optional<Users> emailRecord = repo.findByEmail(email);
        return emailRecord.isPresent();
    }

    @Override
    public String generatePwd() {
        SecureRandom secureRandom = new SecureRandom();
        int pwdLength = 8;
        StringBuilder pwd = new StringBuilder();
        pwd.append(upperCase.charAt(secureRandom.nextInt(upperCase.length())));
        pwd.append(lowerCase.charAt(secureRandom.nextInt(lowerCase.length())));
        pwd.append(specialCase.charAt(secureRandom.nextInt(specialCase.length())));
        pwd.append(numericCase.charAt(secureRandom.nextInt(numericCase.length())));
        while(pwd.length() < pwdLength){
            pwd.append(allCharCase.charAt(secureRandom.nextInt(allCharCase.length())));
        }
        char[] shufflePwd= pwd.toString().toCharArray();
        for(int i =0; i < pwdLength; i++){
            int randomIndex = secureRandom.nextInt(shufflePwd.length);
            char temp = shufflePwd[i];
            shufflePwd[i] = shufflePwd[randomIndex];
            shufflePwd[randomIndex] = temp;
        }
        String finalPwd = new String(shufflePwd);
        return finalPwd;
    }

    @Override
    public boolean updatePwd(FirstLoginRequestDto firstLoginRequestDto) throws Exception {
        Optional<Users> user = repo.findByEmail(firstLoginRequestDto.getEmail());
        if(user.isEmpty())
            throw new Exception("Something went wrong, try again later");
        boolean credMatch = passwordEncoder.matches(firstLoginRequestDto.getCurrentPwd(),user.get().getPassword());
        if(!credMatch)
            throw new InvalidCredentialsException("Please enter correct current password");
        if (passwordEncoder.matches(firstLoginRequestDto.getNewPwd(), user.get().getPassword())) {
            throw new Exception("New password cannot be same as old password");
        }
        user.get().setPassword(passwordEncoder.encode(firstLoginRequestDto.getNewPwd()));
        user.get().setIsFirstPwdUpdated(true);
        repo.save(user.get());
        return true;
    }

    @Override
    public LoginResponseDto validateLogin(LoginRequestDto loginRequestDto) throws InvalidCredentialsException {
        Users user = repo.findByEmail(loginRequestDto.getEmail()).orElseThrow(()-> new InvalidCredentialsException("No Record found"));

        boolean credFlag = passwordEncoder.matches(loginRequestDto.getPwd(),user.getPassword());
        if(!credFlag)
                throw new InvalidCredentialsException("Please enter correct password");
        String token = jwtUtil.generateToken(user.getEmail(),
                user.getRole().name(),
                user.getUserId());
        LoginResponseDto response = new LoginResponseDto();
        response.setToken(token);
        response.setUser(modelMapper.map(user,UserResponseDto.class));
        response.setIsFirstPwdUpdated(user.getIsFirstPwdUpdated());
        return response;
    }

    @Override
    public UserResponseDto upsertUserRecord(UserResponseDto userDto) {
        Users user = modelMapper.map(userDto,Users.class);
        Optional<Users> savedUser = Optional.of(repo.save(user));
        UserResponseDto dto = modelMapper.map(savedUser.get(),UserResponseDto.class);
        return dto;
    }
}
