package com.praisomart.backend.auth.dto;


import com.praisomart.backend.auth.enums.OtpPurpose;

public class SendOtpRequestDTO {
    private String identifier;
    private OtpPurpose purpose;// email or phone

    public SendOtpRequestDTO(String identifier) {
        this.identifier = identifier;
    }

    public SendOtpRequestDTO() {
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public OtpPurpose getPurpose() {
        return purpose;
    }

    public void setPurpose(OtpPurpose purpose) {
        this.purpose = purpose;
    }
}
