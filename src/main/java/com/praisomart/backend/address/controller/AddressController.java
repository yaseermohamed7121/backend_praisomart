package com.praisomart.backend.address.controller;

import com.praisomart.backend.address.dto.AddAddressRequestDTO;
import com.praisomart.backend.address.dto.AddressResponseDTO;
import com.praisomart.backend.address.dto.UpdateAddressRequestDTO;
import com.praisomart.backend.address.service.AddressService;
import com.praisomart.backend.auth.security.CustomerUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<AddressResponseDTO> addAddress(
            @AuthenticationPrincipal CustomerUserDetails userDetails,
            @Valid @RequestBody AddAddressRequestDTO dto) {

        if (userDetails == null) {
            throw new RuntimeException("User not authenticated");
        }

        return ResponseEntity.status(201)
                .body(addressService.addAddress(userDetails.getUserId(), dto));
    }

    @GetMapping
    public ResponseEntity<List<AddressResponseDTO>> getAddresses(
            @AuthenticationPrincipal CustomerUserDetails userDetails) {

        if (userDetails == null) {
            throw new RuntimeException("User not authenticated");
        }

        return ResponseEntity.ok(
                addressService.getAddresses(userDetails.getUserId())
        );
    }

    @GetMapping("/default")
    public ResponseEntity<AddressResponseDTO> getDefaultAddress(
            @AuthenticationPrincipal CustomerUserDetails userDetails) {

        if (userDetails == null) {
            throw new RuntimeException("User not authenticated");
        }

        return ResponseEntity.ok(
                addressService.getDefaultAddress(userDetails.getUserId())
        );
    }
    @PutMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> updateAddress(
            @AuthenticationPrincipal CustomerUserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody UpdateAddressRequestDTO dto) {

        if (userDetails == null) {
            throw new RuntimeException("User not authenticated");
        }

        AddressResponseDTO response =
                addressService.updateAddress(userDetails.getUserId(), id, dto);

        return ResponseEntity.ok(response);
    }
}