package com.praisomart.backend.profile.controller;

import com.praisomart.backend.auth.security.CustomerUserDetails;
import com.praisomart.backend.profile.dto.ProfileResponseDTO;
import com.praisomart.backend.profile.dto.UpdateEmailRequestDTO;
import com.praisomart.backend.profile.dto.UpdatePhoneRequestDTO;
import com.praisomart.backend.profile.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService){
        this.profileService=profileService;
    }

    @GetMapping
    public ResponseEntity<ProfileResponseDTO> getProfile(
            @AuthenticationPrincipal CustomerUserDetails userDetails) {

        ProfileResponseDTO response =
                profileService.getProfile(userDetails.getUserId());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/email")
    public ResponseEntity<ProfileResponseDTO> updateEmail(
            @AuthenticationPrincipal CustomerUserDetails userDetails,
            @Valid @RequestBody UpdateEmailRequestDTO dto) {

        ProfileResponseDTO response =
                profileService.updateEmail(userDetails.getUserId(), dto.getEmail());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/phone")
    public ResponseEntity<ProfileResponseDTO> updatePhone(
            @AuthenticationPrincipal CustomerUserDetails userDetails,
            @Valid @RequestBody UpdatePhoneRequestDTO dto) {

        ProfileResponseDTO response =
                profileService.updatePhone(userDetails.getUserId(), dto.getPhone());

        return ResponseEntity.ok(response);
    }
}
