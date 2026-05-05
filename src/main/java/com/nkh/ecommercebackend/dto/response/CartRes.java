package com.nkh.ecommercebackend.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartRes {
    private List<CartItemRes> items;
}
