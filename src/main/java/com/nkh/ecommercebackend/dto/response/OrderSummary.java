package com.nkh.ecommercebackend.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderSummary {
    private BigDecimal subtotal;
    BigDecimal shippingFee;
    BigDecimal discountAmount;
    BigDecimal totalAmount;
}
