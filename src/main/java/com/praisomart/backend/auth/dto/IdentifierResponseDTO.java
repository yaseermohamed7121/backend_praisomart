package com.praisomart.backend.auth.dto;


public class IdentifierResponseDTO {

    private boolean exists;
    private String message;

    public IdentifierResponseDTO(boolean exists,String message){
        this.exists=exists;
        this.message=message;
    }

    public IdentifierResponseDTO() {
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
