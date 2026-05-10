package com.nkh.ecommercebackend.service.factory;

import com.nkh.ecommercebackend.common.PaymentMethod;
import com.nkh.ecommercebackend.service.PaymentMethodStrategy;
import com.nkh.ecommercebackend.service.impl.CodMethodStrategy;
import com.nkh.ecommercebackend.service.impl.MomoMethodStrategy;
import com.nkh.ecommercebackend.service.impl.StripeMethodStrategy;
import com.nkh.ecommercebackend.service.impl.VNPayMethodStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentMethodStrategyFactory {
    private final CodMethodStrategy codMethodStrategy;
    private final MomoMethodStrategy momoMethodStrategy;
    private final VNPayMethodStrategy vnPayMethodStrategy;
    private final StripeMethodStrategy stripeMethodStrategy;

    public PaymentMethodStrategy create(PaymentMethod paymentMethod) {
        return switch (paymentMethod){
            case PaymentMethod.COD -> codMethodStrategy;
            case PaymentMethod.MOMO -> momoMethodStrategy;
            case PaymentMethod.VNPAY ->  vnPayMethodStrategy;
            case PaymentMethod.STRIPE ->  stripeMethodStrategy;
        };
    }
}
