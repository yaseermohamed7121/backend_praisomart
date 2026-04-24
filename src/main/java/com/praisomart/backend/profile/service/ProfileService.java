package com.praisomart.backend.profile.service;

import com.praisomart.backend.auth.entity.User;
import com.praisomart.backend.auth.repository.UserRepository;
import com.praisomart.backend.profile.dto.ProfileResponseDTO;
import com.praisomart.backend.exception.*;

import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private UserRepository userRepository;

    public ProfileService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public ProfileResponseDTO getProfile(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return new ProfileResponseDTO(
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber()
        );
    }

    public ProfileResponseDTO updateEmail(Long userId, String email) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // avoid null crash + unnecessary DB call
        if (email != null && !email.equals(user.getEmail())) {

            if (userRepository.findByEmail(email).isPresent()) {
                throw new ConflictException("Email already exists");
            }

            user.setEmail(email);
            userRepository.save(user);
        }

        return new ProfileResponseDTO(
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber()
        );
    }

    public ProfileResponseDTO updatePhone(Long userId, String phone) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setPhoneNumber(phone);
        userRepository.save(user);

        return new ProfileResponseDTO(
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber()
        );
    }
}