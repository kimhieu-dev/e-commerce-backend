package com.nkh.ecommercebackend.dto.response;

import com.nkh.ecommercebackend.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartRes implements Serializable {
    private List<CartItem> cartItems;
    private CartSummaryRes checkoutSummary;
}
