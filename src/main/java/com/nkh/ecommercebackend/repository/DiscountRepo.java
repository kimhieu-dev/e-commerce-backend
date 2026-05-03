package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.dto.response.DiscountRes;
import com.nkh.ecommercebackend.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountRepo extends JpaRepository<Discount, String> {

    Optional<Discount> findByCode(String code);
}
