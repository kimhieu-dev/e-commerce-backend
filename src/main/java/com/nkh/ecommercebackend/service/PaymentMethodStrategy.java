package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.common.PaymentMethod;
import com.nkh.ecommercebackend.common.PaymentStatus;

public interface PaymentMethodStrategy {
    PaymentStatus apply(PaymentMethod paymentMethod);
}
