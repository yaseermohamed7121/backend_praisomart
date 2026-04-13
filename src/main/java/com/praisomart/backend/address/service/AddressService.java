package com.praisomart.backend.address.service;

import com.praisomart.backend.address.dto.AddAddressRequestDTO;
import com.praisomart.backend.address.dto.AddressResponseDTO;
import com.praisomart.backend.address.dto.UpdateAddressRequestDTO;
import com.praisomart.backend.address.entity.Address;
import com.praisomart.backend.address.repository.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Transactional
    public AddressResponseDTO addAddress(Long userId, AddAddressRequestDTO dto) {

        if (userId == null) {
            throw new RuntimeException("Invalid user");
        }

        List<Address> addressList =
                addressRepository.findByUserIdAndIsActiveTrue(userId);

        Address address = new Address();

        // First address → auto default
        if (addressList.isEmpty()) {
            address.setIsDefault(true);
        } else {
            if (Boolean.TRUE.equals(dto.getIsDefault())) {
                for (Address a : addressList) {
                    a.setIsDefault(false);
                }
                address.setIsDefault(true);
            } else {
                address.setIsDefault(false);
            }
        }

        address.setUserId(userId);
        address.setFullName(dto.getFullName());
        address.setPhoneNumber(dto.getPhoneNumber());
        address.setPincode(dto.getPincode());
        address.setAddressLine1(dto.getAddressLine1());
        address.setAddressLine2(dto.getAddressLine2());
        address.setCity(dto.getCity());
        address.setState(dto.getState());

        addressRepository.save(address);

        return mapToDTO(address);
    }

    // ✅ Get All Addresses
    public List<AddressResponseDTO> getAddresses(Long userId) {

        List<Address> addressList =
                addressRepository.findByUserIdAndIsActiveTrueOrderByIsDefaultDesc(userId);

        List<AddressResponseDTO> responseList = new ArrayList<>();

        for (Address a : addressList) {
            responseList.add(mapToDTO(a));
        }

        return responseList;
    }

    // ✅ Get Default Address
    public AddressResponseDTO getDefaultAddress(Long userId) {

        Address address = addressRepository
                .findByUserIdAndIsDefaultTrueAndIsActiveTrue(userId)
                .orElse(null);

        if (address == null) {
            return new AddressResponseDTO();
        }

        return mapToDTO(address);
    }

    // ✅ Validate Address
    public Address validateAddress(Long userId, Long addressId) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized address");
        }

        if (!address.getIsActive()) {
            throw new RuntimeException("Address inactive");
        }

        return address;
    }

    // ✅ Mapping
    private AddressResponseDTO mapToDTO(Address a) {

        AddressResponseDTO dto = new AddressResponseDTO();

        dto.setId(a.getId());
        dto.setFullName(a.getFullName());
        dto.setPhoneNumber(a.getPhoneNumber());
        dto.setPincode(a.getPincode());
        dto.setAddressLine1(a.getAddressLine1());
        dto.setAddressLine2(a.getAddressLine2());
        dto.setCity(a.getCity());
        dto.setState(a.getState());
        dto.setIsDefault(a.getIsDefault());

        return dto;
    }

    @Transactional
    public AddressResponseDTO updateAddress(Long userId, Long addressId, UpdateAddressRequestDTO dto) {

        if (userId == null) {
            throw new RuntimeException("Invalid user");
        }

        // ✅ Validate ownership + active
        Address address = validateAddress(userId, addressId);

        // ✅ Fetch all user addresses
        List<Address> addressList =
                addressRepository.findByUserIdAndIsActiveTrue(userId);

        // ✅ Handle default logic
        if (Boolean.TRUE.equals(dto.getIsDefault())) {

            for (Address a : addressList) {
                a.setIsDefault(false);
            }

            address.setIsDefault(true);
        }

        // ✅ Update fields
        address.setFullName(dto.getFullName());
        address.setPhoneNumber(dto.getPhoneNumber());
        address.setPincode(dto.getPincode());
        address.setAddressLine1(dto.getAddressLine1());
        address.setAddressLine2(dto.getAddressLine2());
        address.setCity(dto.getCity());
        address.setState(dto.getState());

        addressRepository.save(address);

        return mapToDTO(address);
    }

}