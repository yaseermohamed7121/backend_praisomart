package com.praisomart.backend.auth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "otp_verification", schema = "paisomart_auth")

public class OtpVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String identifier;  // email or phone

    private String otpCode;     // 6-digit OTP

    private String otpType;     // EMAIL or PHONE

    private LocalDateTime createdAt;

    private LocalDateTime expiryTime;

    private boolean verified;

    @Column(name = "attempts")
    private int attempts;

    @Column(name = "resend_count")
    private int resendCount;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.expiryTime = LocalDateTime.now().plusMinutes(5);
        this.verified = false;
        this.attempts = 0;
        this.resendCount = 0;
    }

    public OtpVerification() {
    }

    public OtpVerification(LocalDateTime createdAt, LocalDateTime expiryTime, Long id, String identifier, String otpCode, String otpType, boolean verified) {
        this.createdAt = createdAt;
        this.expiryTime = expiryTime;
        this.id = id;
        this.identifier = identifier;
        this.otpCode = otpCode;
        this.otpType = otpType;
        this.verified = verified;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public String getOtpType() {
        return otpType;
    }

    public void setOtpType(String otpType) {
        this.otpType = otpType;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public int getResendCount() {
        return resendCount;
    }

    public void setResendCount(int resendCount) {
        this.resendCount = resendCount;
    }
}