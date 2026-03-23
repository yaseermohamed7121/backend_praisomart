package com.praisomart.backend.auth.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class IdentifierRequestDTO {

    private String identifier;

    public String getIdentifier() {
        return identifier;
    }

}
