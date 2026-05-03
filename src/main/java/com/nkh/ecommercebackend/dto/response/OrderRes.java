package com.nkh.ecommercebackend.dto.response;

import com.nkh.ecommercebackend.common.OrderStatus;
import com.nkh.ecommercebackend.common.PaymentMethod;
import com.nkh.ecommercebackend.common.PaymentStatus;
import com.nkh.ecommercebackend.entity.Address;
import com.nkh.ecommercebackend.entity.Discount;
import com.nkh.ecommercebackend.entity.OrderItem;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRes implements Serializable {
    private String trackingNumber;

    private PaymentMethod paymentMethod;

    private OrderStatus status;

    private PaymentStatus paymentStatus;

    private BigDecimal totalPrice;
    private BigDecimal shippingFee;
    private BigDecimal discountAmount;
    private BigDecimal grandTotal;

    private DiscountRes discount;

    private LocalDate estimatedDelivery;

    private CarrierRes carrier;

    private AddressRes address;

    private List<OrderItemRes> orderItems;
}
