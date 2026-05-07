package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.dto.response.DiscountRes;
import com.nkh.ecommercebackend.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DiscountRepo extends JpaRepository<Discount, String> {

    @Modifying
    @Query("update Discount d set d.usedCount = d.usedCount+1 where d.id = :id and d.usedCount<d.usageLimit")
    int increaseUserDiscount(String id);

    Optional<Discount> findByCode(String code);
}
