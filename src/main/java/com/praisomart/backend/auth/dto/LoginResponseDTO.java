package com.praisomart.backend.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String message;
    private String jwtToken;

    // constructor for only message
    public LoginResponseDTO(String message) {
        this.message = message;
    }

    public LoginResponseDTO(String jwtToken, String message) {
        this.jwtToken = jwtToken;
        this.message = message;
    }
}
