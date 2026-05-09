package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.common.DiscountType;
import com.nkh.ecommercebackend.service.DiscountStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PercentageStrategy implements DiscountStrategy {
    private final BigDecimal discountPercentage;
    public PercentageStrategy(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    @Override
    public BigDecimal calculate(BigDecimal subTotal) {
        return discountPercentage.multiply(subTotal).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);
    }
}
