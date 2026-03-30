package com.praisomart.backend.auth.dto;

public class OtpVerifyResponseDTO {

    private String message;

    public OtpVerifyResponseDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
