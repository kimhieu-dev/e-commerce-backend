package com.nkh.ecommercebackend.dto.request;

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

    private String cartId;

    private String productId;

    private Integer quantity;

}
