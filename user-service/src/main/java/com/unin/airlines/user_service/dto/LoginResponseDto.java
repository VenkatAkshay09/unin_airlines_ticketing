package com.unin.airlines.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponseDto {

    private String token;
    private Boolean isFirstPwdUpdated;
    private UserResponseDto user;
}
