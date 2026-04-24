package com.praisomart.backend.auth.entity;

import com.praisomart.backend.auth.enums.OtpChannel;
import com.praisomart.backend.auth.enums.OtpPurpose;
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

    @Enumerated(EnumType.STRING)
    private OtpChannel channel;

    @Enumerated(EnumType.STRING)
    private OtpPurpose purpose;     // EMAIL or PHONE

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
        this.createdAt = LocalDateTime.now();;
        this.expiryTime = LocalDateTime.now().plusMinutes(5);
        this.verified = false;
        this.attempts = 0;
        this.resendCount = 0;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public OtpChannel getChannel() {
        return channel;
    }

    public void setChannel(OtpChannel channel) {
        this.channel = channel;
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

    public OtpPurpose getPurpose() {
        return purpose;
    }

    public void setPurpose(OtpPurpose purpose) {
        this.purpose = purpose;
    }

    public int getResendCount() {
        return resendCount;
    }

    public void setResendCount(int resendCount) {
        this.resendCount = resendCount;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}