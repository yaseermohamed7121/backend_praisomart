package com.praisomart.backend.auth.service;

import com.praisomart.backend.auth.dto.*;
import com.praisomart.backend.auth.entity.OtpVerification;
import com.praisomart.backend.auth.entity.User;
import com.praisomart.backend.auth.repository.OtpVerificationRepository;
import com.praisomart.backend.auth.repository.UserRepository;
import com.praisomart.backend.auth.security.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final OtpVerificationRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthService(UserRepository userRepository,
                       OtpVerificationRepository otpRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       AuthenticationManager authenticationManager,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil=jwtUtil;
        this.authenticationManager=authenticationManager;
        this.emailService=emailService;
    }

    // ------------------------------------------------
    // Generate OTP
    // ------------------------------------------------
    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    // ------------------------------------------------
    // CHECK USER
    // ------------------------------------------------
    public IdentifierResponseDTO checkUser(String identifier) {

        Optional<User> user;

        if(identifier.matches("^[0-9]{10}$")){
            user = userRepository.findByPhoneNumber(identifier);
        }
        else if(identifier.contains("@")){
            user = userRepository.findByEmail(identifier);
        }
        else{
            return new IdentifierResponseDTO(false,"Enter valid email or phone number");
        }
        if(user.isPresent()){
            return new IdentifierResponseDTO(true,"login");
        }

        return new IdentifierResponseDTO(false,"register");
    }

    // ------------------------------------------------
    // SEND OTP
    // ------------------------------------------------
    @Transactional
    public SendOtpResponseDTO sendOtp(String identifier) {

        String otpType;

        if (identifier.matches("^[0-9]{10}$")) {
            otpType = "PHONE";
        } else if (identifier.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            otpType = "EMAIL";
        } else {
            throw new RuntimeException("Invalid identifier");
        }

        Optional<OtpVerification> existingOtp =
                otpRepository.findTopByIdentifierOrderByCreatedAtDesc(identifier);

        // 🔥 30 sec rate limit
        if (existingOtp.isPresent() &&
                existingOtp.get().getCreatedAt().plusSeconds(30).isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Please wait 30 seconds before requesting OTP again");
        }

        // 🔥 Max resend limit
        if (existingOtp.isPresent() &&
                existingOtp.get().getResendCount() >= 5) {
            throw new RuntimeException("Too many OTP requests. Try again later");
        }

        // delete old OTP
        otpRepository.deleteByIdentifier(identifier);

        String otp = generateOtp();

        OtpVerification otpEntity = new OtpVerification();
        otpEntity.setIdentifier(identifier);
        otpEntity.setOtpCode(passwordEncoder.encode(otp));
        otpEntity.setOtpType(otpType);

        // increment resend count
        otpEntity.setResendCount(
                existingOtp.map(o -> o.getResendCount() + 1).orElse(1)
        );

        otpRepository.save(otpEntity);

        if (otpType.equals("EMAIL")) {
            emailService.sendOtpEmail(identifier, otp);
        }

        return new SendOtpResponseDTO("OTP sent successfully");
    }

    // ------------------------------------------------
    // VERIFY OTP
    // ------------------------------------------------
    @Transactional
    public OtpVerifyResponseDTO verifyOtp(String identifier, String otp) {

        OtpVerification otpVerification = otpRepository
                .findTopByIdentifierOrderByCreatedAtDesc(identifier)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        // 🔥 Attempt limit
        if (otpVerification.getAttempts() >= 3) {
            throw new RuntimeException("Too many attempts. Request new OTP");
        }

        // 🔥 Expiry check
        if (otpVerification.getExpiryTime().isBefore(LocalDateTime.now())) {
            otpRepository.save(otpVerification);
            throw new RuntimeException("OTP expired");
        }

        // 🔥 OTP match
        if (!passwordEncoder.matches(otp, otpVerification.getOtpCode())) {

            // increment attempt
            otpVerification.setAttempts(otpVerification.getAttempts() + 1);
            otpRepository.save(otpVerification);
            throw new RuntimeException("Invalid OTP");
        }

        // ✅ Success
        otpVerification.setVerified(true);
        otpRepository.save(otpVerification);

        return new OtpVerifyResponseDTO("OTP verified successfully");
    }

    // ------------------------------------------------
    // REGISTER USER
    // ------------------------------------------------
    public RegisterResponseDTO register(RegisterRequestDTO request){

        if(!request.getPassword().equals(request.getConfirmPassword())){
            return new RegisterResponseDTO("Passwords do not match", null);
        }

        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            return new RegisterResponseDTO("Email already registered",null);
        }

        if(userRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()){
            return new RegisterResponseDTO("Phone already registered",null);
        }

        Optional<OtpVerification> emailOtp =
                otpRepository.findTopByIdentifierOrderByCreatedAtDesc(request.getEmail());

        Optional<OtpVerification> phoneOtp =
                otpRepository.findTopByIdentifierOrderByCreatedAtDesc(request.getPhoneNumber());

        if(emailOtp.isEmpty() || !emailOtp.get().isVerified()){
            return new RegisterResponseDTO("Email not verified", null);
        }

        if(phoneOtp.isEmpty() || !phoneOtp.get().isVerified()){
            return new RegisterResponseDTO("Phone not verified", null);
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        // JWT generation will come here later
        String token = jwtUtil.generateToken(
                user.getId(),
                user.getEmail(),
                List.of(user.getRole())
        );

        return new RegisterResponseDTO("Registration successful", token);
    }

    // ------------------------------------------------
    // LOGIN
    // ------------------------------------------------
    public LoginResponseDTO login(LoginRequestDTO request) {

        // Step 1: Find user (email OR phone)
        User user = userRepository.findByEmail(request.getIdentifier())
                .or(() -> userRepository.findByPhoneNumber(request.getIdentifier()))
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Step 2: Authenticate
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),   // always email
                        request.getPassword()
                )
        );

        // Step 3: Generate JWT
        String token = jwtUtil.generateToken(
                user.getId(),
                user.getEmail(),
                List.of(user.getRole())
        );

        // Step 4: Response
        return new LoginResponseDTO(
                token,
                "Login Successful"
        );
    }

}