package com.unin.airlines.user_service.dto;


import com.unin.airlines.user_service.entity.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponseDto {
    private String firstName;
    private String lastName;
    private String email;
    private Integer age;
    private String phNumber;
    private Roles role;
    private Boolean isFirstPwdUpdated;
    private LocalDateTime createdDateTime;


}
