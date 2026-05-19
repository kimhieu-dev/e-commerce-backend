package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.entity.Carrier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarrierRepo extends JpaRepository<Carrier,String> {
    Optional<Carrier> findByName(String name);
}
