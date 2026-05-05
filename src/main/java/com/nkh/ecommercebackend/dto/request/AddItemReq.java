package com.nkh.ecommercebackend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddItemReq implements Serializable {

    @NotBlank(message = "PRODUCT_ID_BLANK")
    private String productId;

    @Min(value = 1,message = "QUANTITY_INVALID")
    private Integer quantity;

}
