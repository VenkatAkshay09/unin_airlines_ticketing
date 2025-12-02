package com.unin.airlines.user_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.LocalDateTime;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank
    @Size(min=3, max=50)
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Name can only contain alphabets and spaces")
    private String firstName;

    @Pattern(regexp = "^[A-Za-z ]+$", message = "Name can only contain alphabets and spaces")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Roles role = Roles.USER;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    @Max(150)
    @Min(1)
    private Integer age;

    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    @Column(length = 10, nullable = false, unique = true)
    private String phNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CountryCodes countryCode;

    @JsonIgnore
    private String password;

    @Builder.Default
    private Boolean isFirstPwdUpdated = false;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
