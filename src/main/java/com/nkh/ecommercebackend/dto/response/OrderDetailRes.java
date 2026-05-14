package com.nkh.ecommercebackend.dto.response;

import com.nkh.ecommercebackend.common.OrderStatus;
import com.nkh.ecommercebackend.common.PaymentMethod;
import com.nkh.ecommercebackend.common.PaymentStatus;
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
public class OrderDetailRes implements Serializable {
    private String trackingNumber;

    private PaymentMethod paymentMethod;

    private OrderStatus status;

    private PaymentStatus paymentStatus;

    private BigDecimal grandTotal;

    private LocalDate estimatedDelivery;

    private AddressRes address;

    private List<OrderItemRes> orderItems;

    private List<TrackingLogRes> trackingLogs;
}
