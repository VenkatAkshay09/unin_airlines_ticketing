package com.unin.airlines.user_service.dto;

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
    private String countryCode;
}
