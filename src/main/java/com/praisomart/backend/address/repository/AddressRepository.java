package com.praisomart.backend.address.repository;

import com.praisomart.backend.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUserIdAndIsActiveTrue(Long userId);

    List<Address>findByUserIdAndIsActiveTrueOrderByIsDefaultDesc(Long userId);

    Optional<Address> findByUserIdAndIsDefaultTrueAndIsActiveTrue(Long userId);

}
