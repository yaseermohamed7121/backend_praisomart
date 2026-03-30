package com.praisomart.backend.auth.service;

import com.praisomart.backend.auth.dto.*;
import com.praisomart.backend.auth.entity.OtpVerification;
import com.praisomart.backend.auth.entity.User;
import com.praisomart.backend.auth.repository.OtpVerificationRepository;
import com.praisomart.backend.auth.repository.UserRepository;
import com.praisomart.backend.auth.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final OtpVerificationRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       OtpVerificationRepository otpRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil=jwtUtil;
        this.authenticationManager=authenticationManager;
    }

    // ------------------------------------------------
    // Generate OTP
    // ------------------------------------------------
    private String generateOtp() {
        Random random = new Random();
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
    public SendOtpResponseDTO sendOtp(String identifier){

        String otpType;

        if(identifier.matches("^[0-9]{10}$")){
            otpType = "PHONE";
        }
        else if(identifier.contains("@")){
            otpType = "EMAIL";
        }
        else{
            return new SendOtpResponseDTO("Invalid identifier");
        }

        // delete old OTP
        otpRepository.deleteByIdentifier(identifier);

        String otp = generateOtp();

        OtpVerification otpEntity = new OtpVerification();
        otpEntity.setIdentifier(identifier);
        otpEntity.setOtpCode(otp);
        otpEntity.setOtpType(otpType);

        otpRepository.save(otpEntity);

        // TODO: integrate email / sms service
        System.out.println("OTP for " + identifier + " : " + otp);

        return new SendOtpResponseDTO("OTP sent successfully");
    }

    // ------------------------------------------------
    // VERIFY OTP
    // ------------------------------------------------
    public OtpVerifyResponseDTO verifyOtp(String identifier, String otp){

        Optional<OtpVerification> otpRecord =
                otpRepository.findTopByIdentifierOrderByCreatedAtDesc(identifier);

        if(otpRecord.isEmpty()){
            return new OtpVerifyResponseDTO("OTP not found");
        }

        OtpVerification otpVerification = otpRecord.get();

        if(otpVerification.getExpiryTime().isBefore(LocalDateTime.now())){
            return new OtpVerifyResponseDTO("OTP expired");
        }

        if(!otpVerification.getOtpCode().equals(otp)){
            return new OtpVerifyResponseDTO("Invalid OTP");
        }

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