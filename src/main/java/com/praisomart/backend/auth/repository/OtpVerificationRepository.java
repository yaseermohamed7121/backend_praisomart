package com.praisomart.backend.auth.repository;

import com.praisomart.backend.auth.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {

//    Optional<OtpVerification> findByIdentifierAndOtpType(String identifier, String otpType);

    Optional<OtpVerification> findTopByIdentifierOrderByCreatedAtDesc(String identifier);

    void deleteByIdentifier(String identifier);
}
