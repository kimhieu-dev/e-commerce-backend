package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.entity.Discount;
import com.nkh.ecommercebackend.service.DiscountStrategy;

import java.math.BigDecimal;

public class PercentageStrategy implements DiscountStrategy {
    @Override
    public BigDecimal calculate(Discount discount) {
        return null;
    }
}
