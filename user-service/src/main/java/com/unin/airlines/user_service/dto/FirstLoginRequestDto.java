package com.unin.airlines.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FirstLoginRequestDto {

    @Email
    @NotBlank(message = "Email can't be blank")
    private String email;

    @NotBlank(message = "Password can't be blank")
    @Size(min=8, message = "Password must be at least 8 characters long")
    private String currentPwd;

    @NotBlank(message = "Password can't be blank")
    private String newPwd;
}
