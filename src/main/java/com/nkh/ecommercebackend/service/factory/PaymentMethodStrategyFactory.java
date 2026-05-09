package com.nkh.ecommercebackend.service.factory;

import com.nkh.ecommercebackend.common.PaymentMethod;
import com.nkh.ecommercebackend.service.PaymentMethodStrategy;
import com.nkh.ecommercebackend.service.impl.CodMethodStrategy;
import com.nkh.ecommercebackend.service.impl.MomoMethodStrategy;
import com.nkh.ecommercebackend.service.impl.StripeMethodStrategy;
import com.nkh.ecommercebackend.service.impl.VNPayMethodStrategy;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodStrategyFactory {
    public PaymentMethodStrategy create(PaymentMethod paymentMethod) {
        return switch (paymentMethod){
            case PaymentMethod.COD -> new CodMethodStrategy();
            case PaymentMethod.MOMO -> new MomoMethodStrategy();
            case PaymentMethod.VNPAY ->  new VNPayMethodStrategy();
            case PaymentMethod.STRIPE ->  new StripeMethodStrategy();
        };
    }
}
