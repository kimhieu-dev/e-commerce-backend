package com.nkh.ecommercebackend.dto.request;

import com.nkh.ecommercebackend.common.OrderStatus;
import com.nkh.ecommercebackend.common.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderFilterReq implements Serializable {
    private String trackingNumber;
    private OrderStatus orderStatus;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private PaymentStatus paymentStatus;
}
