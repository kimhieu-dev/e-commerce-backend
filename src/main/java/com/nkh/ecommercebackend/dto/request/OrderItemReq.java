package com.nkh.ecommercebackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class OrderItemReq implements Serializable {
    @NotBlank
    private String productId;

    @NotNull
    private Integer quantity;
}
