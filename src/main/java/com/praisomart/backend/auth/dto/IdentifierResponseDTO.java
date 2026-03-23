package com.praisomart.backend.auth.dto;

import lombok.Data;

@Data
public class IdentifierResponseDTO {

    private boolean exists;
    private String message;

    public IdentifierResponseDTO(boolean exists,String message){
        this.exists=exists;
        this.message=message;
    }

}
