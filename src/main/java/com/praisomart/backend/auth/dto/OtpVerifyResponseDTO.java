package com.praisomart.backend.auth.dto;

import lombok.Data;

@Data
public class OtpVerifyResponseDTO {

    private String message;

    public OtpVerifyResponseDTO(String message) {
        this.message = message;
    }


}
