package com.praisomart.backend.auth.dto;

public class IdentifierRequestDTO {

    private String identifier;

    public IdentifierRequestDTO(String identifier) {
        this.identifier = identifier;
    }

    public IdentifierRequestDTO() {
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
