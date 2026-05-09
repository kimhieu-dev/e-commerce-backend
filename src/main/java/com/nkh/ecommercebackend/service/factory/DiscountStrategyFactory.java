package com.nkh.ecommercebackend.service.factory;

import com.nkh.ecommercebackend.common.DiscountType;
import com.nkh.ecommercebackend.service.DiscountStrategy;
import com.nkh.ecommercebackend.service.impl.FixedAmountStrategy;
import com.nkh.ecommercebackend.service.impl.PercentageStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DiscountStrategyFactory {

    public DiscountStrategy create (DiscountType discountType, BigDecimal value) {
        return switch (discountType){
            case DiscountType.PERCENTAGE -> new PercentageStrategy(value);
            case DiscountType.FIXED_AMOUNT ->  new FixedAmountStrategy(value);
        };
    }
}
