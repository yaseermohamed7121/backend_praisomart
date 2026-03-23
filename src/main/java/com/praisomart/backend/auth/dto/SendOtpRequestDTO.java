package com.praisomart.backend.auth.dto;

import lombok.Data;

@Data
public class SendOtpRequestDTO {
    private String identifier; // email or phone

    public String getIdentifier() {
        return identifier;
    }
}
