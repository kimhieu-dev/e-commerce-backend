package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.dto.request.AddItemReq;
import com.nkh.ecommercebackend.dto.request.UpdateCartItemReq;
import com.nkh.ecommercebackend.dto.response.CartItemRes;
import com.nkh.ecommercebackend.dto.response.CartRes;
import com.nkh.ecommercebackend.dto.response.SummaryRes;
import com.nkh.ecommercebackend.entity.CartItem;

public interface CartService {
    CartRes getCurrentCart();

    CartItemRes addItem(AddItemReq request);

    void deleteItem(String id);

    CartItemRes updateQuantityItem(String id, UpdateCartItemReq request);

}
