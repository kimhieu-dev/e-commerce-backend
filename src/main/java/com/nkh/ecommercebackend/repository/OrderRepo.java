package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {

    @Query("""
            select sum (o.grandTotal) from Order o
                        where o.status = OrderStatus.DELIVERED
                                    and o.createdAt between :from and :to and o.deleted = false
            """)
    BigDecimal calculateTotalRevenue(LocalDateTime from, LocalDateTime to);

    @Query("""
            select count (o.id) from Order o
                        where o.deleted = false
                                    and o.createdAt between :from and :to
            """)
    Integer countTotalOrders(LocalDateTime from, LocalDateTime to);

    @Query("""
            select count (o.id) from Order o
                        where o.deleted = false
                                    and o.status = OrderStatus.PENDING
                                                and o.createdAt between :from and :to
            """)
    Integer countTotalPendingOrders(LocalDateTime from, LocalDateTime to);

    @Query("""
            select count (o.id) from Order o
                        where o.deleted = false
                                    and o.status = OrderStatus.SHIPPING
                                                and o.createdAt between :from and :to
            """)
    Integer countTotalShippingOrders(LocalDateTime from, LocalDateTime to);

    @Query("""
            select count (o.id) from Order o
                        where o.deleted = false
                                    and o.status = OrderStatus.FAILED
                                                and o.createdAt between :from and :to
            """)
    Integer countTotalFailedOrders(LocalDateTime from, LocalDateTime to);

    @Query("""
            select count (o.id) from Order o
                        where o.deleted = false
                                    and o.status = OrderStatus.CONFIRMED
                                                and o.createdAt between :from and :to
            """)
    Integer countTotalConfirmedOrders(LocalDateTime from, LocalDateTime to);

    @Query("""
                select o from Order o
                left join fetch o.trackingLogs
                join fetch o.address
                join fetch o.orderItems
                where o.id = :id
            """)
    Optional<Order> findByIdTrackingLog(String id);
}
