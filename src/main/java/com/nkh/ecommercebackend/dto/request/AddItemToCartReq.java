package com.nkh.ecommercebackend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddItemToCartReq implements Serializable {

    @NotBlank(message = "CART_ID_BLANK")
    private String cartId;

    @NotBlank(message = "PRODUCT_ID_BLANK")
    private String productId;

    @Min(value = 1,message = "QUANTITY_INVALID")
    private Integer quantity;

}
