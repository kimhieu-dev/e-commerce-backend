package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.entity.Discount;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DiscountRepo extends JpaRepository<Discount, String> {

    @Modifying
    @Query("update Discount d set d.usedCount = d.usedCount+1 where d.id = :id and d.usageLimit - d.usedCount - d.reservedCount > 0")
    int increaseUsedCount(String id);

    //như thế này giúp xoá entity cũ lưu ở cache
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Discount d set d.reservedCount = d.reservedCount+1 where d.id = :id and d.usageLimit - d.usedCount - d.reservedCount > 0")
    int increaseReservedCount(String id);

    @Modifying
    @Query("update Discount d set d.reservedCount = d.reservedCount-1 where d.id = :id and d.reservedCount>0")
    int decreaseReservedCount(String id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Discount> findByCode(String code);

    @Query("""
            select d from Discount d
            where d.deleted = false
            and current_timestamp >= d.startDate
            and current_timestamp <= d.endDate""")
    List<Discount> findAllUsableDiscounts();

    @Modifying
    @Query("""
            update Discount d
            set
                d.usedCount = d.usedCount + 1,
                d.reservedCount = d.reservedCount - 1
            where d.id = :id
            and d.reservedCount > 0
            """)
    int updateUsedCountAndReservedCount(String id);

}
