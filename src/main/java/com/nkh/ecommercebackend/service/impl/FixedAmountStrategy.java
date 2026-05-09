package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.service.DiscountStrategy;

import java.math.BigDecimal;

public class FixedAmountStrategy implements DiscountStrategy {
    private final BigDecimal discountAmount;

    public FixedAmountStrategy(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Override
    public BigDecimal calculate(BigDecimal subTotal) {
        return discountAmount;
    }
}
