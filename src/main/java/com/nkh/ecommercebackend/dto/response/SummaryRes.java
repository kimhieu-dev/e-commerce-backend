package com.nkh.ecommercebackend.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public class SummaryRes {
    private BigDecimal subtotal;
    BigDecimal shippingFee;
    BigDecimal discountAmount;
    BigDecimal totalAmount;
}
