package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.common.OrderStatus;
import com.nkh.ecommercebackend.dto.request.OrderOverviewStats;
import com.nkh.ecommercebackend.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
                join fetch o.orderItems
                where o.id = :id
            """)
    Optional<Order> findByIdTrackingLog(String id);

    @Query("""
                select o from Order o where o.user.id = :userId and o.deleted = false order by o.createdAt desc
            """)
    List<Order> findMyOrders(String userId);

    List<Order> findAllByUserIdAndStatusAndDeletedFalse(String userId, OrderStatus status, Pageable pageable);

    List<Order> findAllByUserIdAndDeletedFalse(String userId);

    @Query("""
                select o from Order o
                where o.status = :orderStatus
                    and o.updatedAt between :start and :end
            """)
    List<Order> findOrdersForSendMail(OrderStatus orderStatus, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update Order o
            set o.status = OrderStatus.REJECTED
            where o.id = :id
            and o.status = OrderStatus.PENDING
            """)
    int rejectOrder(String id);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
                update Order o
                set o.status = OrderStatus.CONFIRMED
                where o.id = :id
                and o.status = OrderStatus.PENDING
            """)
    int approveOrder(String id);

    @Query("""
    SELECT OrderOverviewStats(
        SUM(CASE WHEN o.status = OrderStatus.DELIVERED THEN o.grandTotal ELSE 0 END),
        COUNT(o.id),
        SUM(CASE WHEN o.status = OrderStatus.PENDING  THEN 1 ELSE 0 END),
        SUM(CASE WHEN o.status = OrderStatus.SHIPPING THEN 1 ELSE 0 END),
        SUM(CASE WHEN o.status = OrderStatus.FAILED   THEN 1 ELSE 0 END)
    )
    FROM Order o
    WHERE o.deleted = false
      AND o.createdAt BETWEEN :from AND :to
""")
    OrderOverviewStats getOverviewStats(LocalDateTime from, LocalDateTime to);
}
