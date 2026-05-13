package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.common.PaymentMethod;
import com.nkh.ecommercebackend.common.PaymentStatus;
import com.nkh.ecommercebackend.service.PaymentMethodStrategy;
import org.springframework.stereotype.Component;

@Component
public class VNPayMethodStrategy implements PaymentMethodStrategy {
    @Override
    public PaymentStatus apply(PaymentMethod paymentMethod) {
        return PaymentStatus.AWAITING_PAYMENT;
    }
}
