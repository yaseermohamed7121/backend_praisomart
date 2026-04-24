package com.praisomart.backend.auth.dto;

import com.praisomart.backend.auth.enums.OtpPurpose;

public class OtpVerifyRequestDTO {

    private String identifier;
    private String otp;
    private OtpPurpose purpose;   // ✅ ADD THIS

    public OtpVerifyRequestDTO() {}

    public OtpVerifyRequestDTO(String identifier, String otp, OtpPurpose purpose) {
        this.identifier = identifier;
        this.otp = otp;
        this.purpose = purpose;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public OtpPurpose getPurpose() {
        return purpose;
    }

    public void setPurpose(OtpPurpose purpose) {
        this.purpose = purpose;
    }
}