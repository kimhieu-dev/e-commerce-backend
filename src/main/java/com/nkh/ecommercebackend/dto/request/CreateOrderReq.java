package com.nkh.ecommercebackend.dto.request;

import com.nkh.ecommercebackend.common.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderReq {

    @NotNull(message = "PAYMENT_METHOD_NULL")
    private PaymentMethod paymentMethod = PaymentMethod.COD;

    @NotBlank(message = "DISCOUNT_CODE_BLANK")
    private String discountCode;

    @NotBlank(message = "CARRIER_BLANK")
    private String carrierId;

    @NotBlank(message = "ADDRESS_BLANK")
    private String addressId;
}
