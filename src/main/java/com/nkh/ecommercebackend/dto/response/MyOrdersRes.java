package com.nkh.ecommercebackend.dto.response;

import com.nkh.ecommercebackend.common.OrderStatus;
import com.nkh.ecommercebackend.common.PaymentMethod;
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
public class MyOrdersRes implements Serializable {
    private String trackingNumber;
    private String createAt;
    private PaymentMethod paymentMethod;
    private BigDecimal grandTotal;
    private OrderStatus status;
}
