package com.praisomart.backend.auth.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RegisterRequestDTO {
    @NotBlank(message = "enter name")
    private String name;
    @NotBlank
    @Email(message = "enter valid email")
    private String email;
    @NotBlank(message ="Enter phn_no")
    private String phoneNumber;
    @NotBlank(message ="Enter password")
    private String password;
    @NotBlank(message ="Enter password to continue")
    private String confirmPassword;

    public RegisterRequestDTO(String confirmPassword, String email, String name, String password, String phoneNumber) {
        this.confirmPassword = confirmPassword;
        this.email = email;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public RegisterRequestDTO() {
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
