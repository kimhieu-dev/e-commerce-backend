package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.entity.Discount;


import java.math.BigDecimal;

public interface DiscountStrategy {
    BigDecimal calculate(Discount discount);
}
