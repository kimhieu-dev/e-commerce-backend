package com.nkh.ecommercebackend.dto.request;

import com.nkh.ecommercebackend.common.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderReq {

    @NotEmpty(message = "ITEMS_EMPTY")
    @Valid
    List<OrderItemReq> orderItems;

    @NotNull(message = "PAYMENT_METHOD_NULL")
    private PaymentMethod paymentMethod;

    @NotBlank(message = "DISCOUNT_CODE_BLANK")
    private String discountCode;

    @NotBlank(message = "ADDRESS_BLANK")
    private String addressId;
}
