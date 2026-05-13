package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface OrderRepo extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {

    @Query("""
            select sum (o.grandTotal) from Order o
                        where o.status = OrderStatus.DELIVERED
                                    and o.createdAt between :from and :to and o.deleted = false
            """)
    BigDecimal calculateTotalRevenue(LocalDate from, LocalDate to);

    @Query("""
            select count (o.id) from Order o
                        where o.deleted = false
                                    and o.createdAt between :from and :to
            """)
    Integer countTotalOrders(LocalDate from, LocalDate to);

    @Query("""
            select count (o.id) from Order o
                        where o.deleted = false
                                    and o.status = OrderStatus.PENDING
                                                and o.createdAt between :from and :to
            """)
    Integer countTotalPendingOrders(LocalDate from, LocalDate to);

    @Query("""
            select count (o.id) from Order o
                        where o.deleted = false
                                    and o.status = OrderStatus.SHIPPING
                                                and o.createdAt between :from and :to
            """)
    Integer countTotalShippingOrders(LocalDate from, LocalDate to);

    @Query("""
            select count (o.id) from Order o
                        where o.deleted = false
                                    and o.status = OrderStatus.FAILED
                                                and o.createdAt between :from and :to
            """)
    Integer countTotalFailedOrders(LocalDate from, LocalDate to);
}
