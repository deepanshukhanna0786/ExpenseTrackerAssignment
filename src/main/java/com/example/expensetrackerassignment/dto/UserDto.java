package com.example.expensetrackerassignment.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import com.example.expensetrackerassignment.enums.Role;

@Getter
@Setter
public class UserDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    private String name;
    private Role role;
}
