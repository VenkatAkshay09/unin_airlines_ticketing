package com.unin.airlines.user_service.service.impl;

import com.unin.airlines.user_service.config.JwtUtil;
import com.unin.airlines.user_service.dto.LoginRequestDto;
import com.unin.airlines.user_service.dto.LoginResponseDto;
import com.unin.airlines.user_service.dto.UserRequestDto;
import com.unin.airlines.user_service.dto.UserResponseDto;
import com.unin.airlines.user_service.entity.CountryCodes;
import com.unin.airlines.user_service.entity.Roles;
import com.unin.airlines.user_service.entity.Users;
import com.unin.airlines.user_service.exception.InvalidCredentialsException;
import com.unin.airlines.user_service.exception.UserExistsException;
import com.unin.airlines.user_service.repository.UserRepo;
import com.unin.airlines.user_service.service.MailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static java.lang.Character.isUpperCase;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.*;


class UserServiceImplTest {

    @Mock
    BCryptPasswordEncoder passwordEncoder;
    @Mock
    MailService mailService;
    @Mock
    JwtUtil jwtUtil;
    @Mock
    UserRepo repo;
    @Mock
    ModelMapper modelMapper;
    @InjectMocks
    UserServiceImpl userService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void fetchRecordByEmail() {

        String email = "venkatakshay@gmail.com";

        Users mockUser = new Users();
        mockUser.setEmail(email);

        Mockito.when(repo.findByEmail(email)).thenReturn(Optional.of(mockUser));

        boolean result = userService.fetchRecordByEmail(email);
        Assertions.assertTrue(result);
    }

    @Test
    void generatePwd(){
        String password = userService.generatePwd();

        assertNotNull(password, "Password should not be null");
        assertEquals(8, password.length(), "Password length should be 8");
        assertTrue(password.chars().anyMatch(Character::isUpperCase),
                "Password should contain at least 1 uppercase letter");
        assertTrue(password.chars().anyMatch(Character::isLowerCase),
                "Password should contain at least 1 lowercase letter");
        assertTrue(password.chars().anyMatch(Character::isDigit),
                "Password should contain at least 1 digit");
        String specialChars = "!@#$%^&*()_+";
        assertTrue(password.chars().anyMatch(c -> specialChars.indexOf(c) >= 0),
                "Password should contain at least 1 special character");
    }

    @Test
    void validateLoginNoRecord(){
        String email = "ven@gmail.com";
        String pwd = "abc@1234";
        LoginRequestDto dto = new LoginRequestDto();
        dto.setEmail(email);
        dto.setPwd(pwd);
        Mockito.when(repo.findByEmail(email)).thenReturn(Optional.empty());
        InvalidCredentialsException ex = assertThrows(InvalidCredentialsException.class, () -> {
            userService.validateLogin(dto);
        });
        assertEquals("No Record found-" + email, ex.getMessage());

    }

    @Test
    void validateLoginInvalidCred(){
        String email = "ven@gmail.com";
        String pwd = "abc@1234";
        LoginRequestDto dto = new LoginRequestDto();
        dto.setEmail(email);
        dto.setPwd(pwd);

        String hashedDtoPwd = passwordEncoder.encode(pwd);
        String hashedPwd = passwordEncoder.encode("dhdjds@123");

        Users mockUser = new Users();
        mockUser.setEmail(email);
        mockUser.setPassword(hashedPwd);

        Mockito.when(repo.findByEmail(email)).thenReturn(Optional.of(mockUser));
        Mockito.when(passwordEncoder.matches(dto.getPwd(), mockUser.getPassword()))
                .thenReturn(false);
        InvalidCredentialsException ex = assertThrows(InvalidCredentialsException.class, () -> {
            userService.validateLogin(dto);
        });
        assertEquals("Please enter correct password-"+ email, ex.getMessage());
    }

    @Test
    void validateLoginCorrectCred() throws Exception{
        String email = "ven@gmail.com";
        String pwd = "abc@1234";
        LoginRequestDto dto = new LoginRequestDto();
        dto.setEmail(email);
        dto.setPwd(pwd);

        String hashedPwd = passwordEncoder.encode(pwd);

        Users mockUser = new Users();
        mockUser.setUserId(1L);
        mockUser.setFirstName("Venkat");
        mockUser.setLastName("Akshay");
        mockUser.setRole(Roles.USER);
        mockUser.setAge(25);
        mockUser.setIsFirstPwdUpdated(false);
        mockUser.setPhNumber("8768907756");
        mockUser.setCountryCode(CountryCodes.AUS);
        mockUser.setEmail(email);
        mockUser.setPassword(hashedPwd);

        Mockito.when(repo.findByEmail(email)).thenReturn(Optional.of(mockUser));
        Mockito.when(passwordEncoder.matches(dto.getPwd(), mockUser.getPassword()))
                .thenReturn(true);

        Mockito.when(jwtUtil.generateToken(email, mockUser.getRole().name(), mockUser.getUserId()))
                .thenReturn("dummyToken");

//        Mockito.when(modelMapper.map(Mockito.eq(mockUser), Mockito.eq(UserResponseDto.class)))
//                .thenReturn(new UserResponseDto());

        LoginResponseDto response = userService.validateLogin(dto);
        assertEquals("dummyToken", response.getToken());
        assertNotNull(response);

    }

    @Test
    void createRecordEmailExists() throws Exception{
        String email = "ven@gmail.com";

        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setEmail(email);

        Users users = new Users();
        users.setEmail(email);

        Mockito.when(repo.findByEmail(email)).thenReturn(Optional.of(users));
        UserExistsException ex = assertThrows(UserExistsException.class,()->userService.createUserRecord(requestDto));


        assertEquals("Email already exists: " +email, ex.getMessage());

    }

    @Test
    void createRecordPhoneNumberExists() throws Exception{
        String phn = "9807866530";
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setPhNumber(phn);

        Users users = new Users();
        users.setPhNumber(phn);
        Mockito.when(repo.findByEmail("abc@gmail.com")).thenReturn(Optional.empty());
        Mockito.when(repo.findByPhNumber(phn)).thenReturn(Optional.of(users));
        UserExistsException ex = assertThrows(UserExistsException.class,()->userService.createUserRecord(requestDto));
        assertEquals("Phone number already exists: "+phn, ex.getMessage());
    }
    

}