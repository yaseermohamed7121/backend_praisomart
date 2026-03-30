package com.praisomart.backend.auth.dto;


public class SendOtpRequestDTO {
    private String identifier; // email or phone

    public SendOtpRequestDTO(String identifier) {
        this.identifier = identifier;
    }

    public SendOtpRequestDTO() {
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
