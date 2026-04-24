package com.praisomart.backend.auth.repository;

import com.praisomart.backend.auth.entity.OtpVerification;
import com.praisomart.backend.auth.enums.OtpPurpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {

//    Optional<OtpVerification> findByIdentifierAndOtpType(String identifier, String otpType);

    Optional<OtpVerification> findTopByIdentifierOrderByCreatedAtDesc(String identifier);

    Optional<OtpVerification>findTopByIdentifierAndPurposeOrderByCreatedAtDesc(String identifier, OtpPurpose purpose);
    void deleteByIdentifier(String identifier);

    void deleteByExpiryTimeBefore(LocalDateTime time);

    void deleteByIdentifierAndPurpose(String identifier,OtpPurpose purpose);
}
