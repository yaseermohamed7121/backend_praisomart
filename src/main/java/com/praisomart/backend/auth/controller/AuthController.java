package com.praisomart.backend.auth.controller;

import com.praisomart.backend.auth.dto.*;
import com.praisomart.backend.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Send OTP
    @PostMapping("/check-user")
    public ResponseEntity<IdentifierResponseDTO> userIdentification(
            @Valid
            @RequestBody IdentifierRequestDTO request) {

        IdentifierResponseDTO response=authService.checkUser(request.getIdentifier());

        return ResponseEntity.ok(response);
    }

    // login
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginRequestDTO request){

        LoginResponseDTO response =
                authService.login(request);

        return ResponseEntity.ok(response);
    }

    // register
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(
            @RequestBody RegisterRequestDTO request){

        RegisterResponseDTO response =
                authService.register(request);

        return ResponseEntity.ok(response);
    }

    // send otp
    @PostMapping("/send-otp")
    public ResponseEntity<SendOtpResponseDTO> sendOtp(
            @RequestBody SendOtpRequestDTO request){

        SendOtpResponseDTO response =
                authService.sendOtp(request.getIdentifier());

        return ResponseEntity.ok(response);
    }

    // verify otp
    @PostMapping("/verify-otp")
    public ResponseEntity<OtpVerifyResponseDTO> verifyOtp(
            @RequestBody OtpVerifyRequestDTO request){

        OtpVerifyResponseDTO response =
                authService.verifyOtp(
                        request.getIdentifier(),
                        request.getOtp());

        return ResponseEntity.ok(response);
    }


}
