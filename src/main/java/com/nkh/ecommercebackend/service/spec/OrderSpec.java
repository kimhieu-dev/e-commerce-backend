package com.nkh.ecommercebackend.service.spec;

import com.nkh.ecommercebackend.common.OrderStatus;
import com.nkh.ecommercebackend.common.PaymentStatus;
import com.nkh.ecommercebackend.entity.Order;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class OrderSpec {
    public static Specification<Order> likeTrackingNumber(String trackingNumber) {
        return new Specification<Order>() {
            @Override
            public @Nullable Predicate toPredicate(@NonNull Root<Order> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
                if (trackingNumber == null || trackingNumber.isEmpty()) {
                    return criteriaBuilder.conjunction();
                }
                return criteriaBuilder.like(root.get("trackingNumber"), "%" + trackingNumber + "%");
            }
        };
    }
    public static Specification<Order> equalOrderStatus(OrderStatus orderStatus) {
        return new Specification<Order>() {
            @Override
            public @Nullable Predicate toPredicate(@NonNull Root<Order> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
                if (orderStatus == null) {
                    return criteriaBuilder.conjunction();
                }
                return criteriaBuilder.equal(root.get("status"), orderStatus);
            }
        };
    }
    public static Specification<Order> equalMinPrice(BigDecimal minPrice) {
        return new Specification<Order>() {
            @Override
            public @Nullable Predicate toPredicate(@NonNull Root<Order> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
                if (minPrice == null) {
                    return criteriaBuilder.conjunction();
                }
                return criteriaBuilder.greaterThanOrEqualTo(root.get("totalPrice"), minPrice);
            }
        };
    }
    public static Specification<Order> equalMaxPrice(BigDecimal maxPrice) {
        return new Specification<Order>() {
            @Override
            public @Nullable Predicate toPredicate(@NonNull Root<Order> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
                if (maxPrice == null) {
                    return criteriaBuilder.conjunction();
                }
                return criteriaBuilder.lessThanOrEqualTo(root.get("totalPrice"), maxPrice);
            }
        };
    }

    public static Specification<Order> equalPaymentStatus(PaymentStatus paymentStatus) {
        return new Specification<Order>() {
            @Override
            public @Nullable Predicate toPredicate(@NonNull Root<Order> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
                if (paymentStatus == null) {
                    return criteriaBuilder.conjunction();
                }
                return criteriaBuilder.equal(root.get("paymentStatus"), paymentStatus);
            }
        };
    }
}
