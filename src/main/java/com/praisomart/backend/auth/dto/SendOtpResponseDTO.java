package com.praisomart.backend.auth.dto;

import lombok.Data;

@Data
public class SendOtpResponseDTO {

    private String message;

    public SendOtpResponseDTO(String message) {
        this.message = message;
    }

}
