package com.praisomart.backend.profile.dto;

import jakarta.validation.constraints.Email;

import jakarta.validation.constraints.NotBlank;

public class UpdateEmailRequestDTO {

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
