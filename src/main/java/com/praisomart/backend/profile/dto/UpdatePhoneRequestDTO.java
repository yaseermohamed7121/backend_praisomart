package com.praisomart.backend.profile.dto;

import jakarta.validation.constraints.Pattern;

import jakarta.validation.constraints.NotBlank;

public class UpdatePhoneRequestDTO {

    @NotBlank(message = "Phone cannot be empty")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid Indian phone number")
    private String phone;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


}