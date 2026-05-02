package com.nkh.ecommercebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartSummaryRes {
    private List<CartItemRes> items;
    private CheckoutRes summary;
}
