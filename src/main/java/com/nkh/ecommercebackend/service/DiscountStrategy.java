package com.nkh.ecommercebackend.service;

import java.math.BigDecimal;

public interface DiscountStrategy {
    BigDecimal calculate(BigDecimal subTotal);
}
