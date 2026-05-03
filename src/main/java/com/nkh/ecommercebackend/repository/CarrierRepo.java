package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.entity.Carrier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarrierRepo extends JpaRepository<Carrier,String> {
}
