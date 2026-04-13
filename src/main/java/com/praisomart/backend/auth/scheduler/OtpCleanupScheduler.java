package com.praisomart.backend.auth.scheduler;

import com.praisomart.backend.auth.repository.OtpVerificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@EnableScheduling
public class OtpCleanupScheduler {

    private final OtpVerificationRepository otpRepository;

    public OtpCleanupScheduler(OtpVerificationRepository otpRepository) {
        this.otpRepository = otpRepository;
    }

    @Scheduled(cron = "0 0 * * * *")
    @Transactional// every hour
    public void cleanExpiredOtp() {
        otpRepository.deleteByExpiryTimeBefore(LocalDateTime.now());
    }
}
