package com.nkh.ecommercebackend.entity;

import com.nkh.ecommercebackend.common.OrderStatus;
import com.nkh.ecommercebackend.common.PaymentMethod;
import com.nkh.ecommercebackend.common.PaymentStatus;
import com.nkh.ecommercebackend.common.ProductStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "tracking_number", nullable = false)
    private String trackingNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method",nullable = false)
    private PaymentMethod paymentMethod = PaymentMethod.COD;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private OrderStatus status =  OrderStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status",nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.UNPAID;

    @Column(name = "total_price",nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "shipping_fee",nullable = false)
    private BigDecimal shippingFee;

    @Column(name = "discount_amount",nullable = false)
    private BigDecimal discountAmount;

    @Column(name = "grand_total",nullable = false)
    private BigDecimal grandTotal;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id",nullable = false)
    private Discount discount;

    @Column(name = "estimated_delivery",nullable = false)
    private LocalDate estimatedDelivery;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrier_id",nullable = false)
    private Carrier carrier;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "order",fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;
}
