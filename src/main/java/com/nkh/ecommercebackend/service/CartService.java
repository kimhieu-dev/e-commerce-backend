package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.dto.request.AddItemToCartReq;
import com.nkh.ecommercebackend.dto.request.UpdateCartItemReq;
import com.nkh.ecommercebackend.dto.response.CartItemRes;
import com.nkh.ecommercebackend.dto.response.CartRes;

public interface CartService {
    CartRes getCurrentCart();

    void addItem( AddItemToCartReq request);

    CartItemRes updateQuantityItem(String id, UpdateCartItemReq request);
}
