package com.praisomart.backend.auth.dto;

public class SendOtpResponseDTO {

    private String message;

    public SendOtpResponseDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
