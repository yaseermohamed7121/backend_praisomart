package com.praisomart.backend.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor

@NoArgsConstructor
public class RegisterResponseDTO {
    private String message;
    private String jwtToken;

    public RegisterResponseDTO(String jwtToken, String message) {
        this.jwtToken = jwtToken;
        this.message = message;
    }
}
