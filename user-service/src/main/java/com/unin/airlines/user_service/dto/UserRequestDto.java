package com.unin.airlines.user_service.dto;

import com.unin.airlines.user_service.entity.CountryCodes;
import com.unin.airlines.user_service.entity.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private Integer age;
    private String phNumber;
    private CountryCodes countryCode;
    private Roles role;
}
