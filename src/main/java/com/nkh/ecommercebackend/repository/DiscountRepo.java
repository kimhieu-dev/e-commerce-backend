package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DiscountRepo extends JpaRepository<Discount, String> {

    @Modifying
    @Query("update Discount d set d.usedCount = d.usedCount+1 where d.id = :id and d.usedCount<d.usageLimit")
    int increaseUsedCount(String id);

    @Modifying
    @Query("update Discount d set d = d.reservedCount+1 where d.id = :id and d.reservedCount<d.usageLimit")
    int increaseReservedCount(String id);

    Optional<Discount> findByCode(String code);

    @Query("""
            select d from Discount d
            where d.deleted = false
            and current_timestamp >= d.startDate
            and current_timestamp <= d.endDate""")
    List<Discount> findAllUsableDiscounts();

}
