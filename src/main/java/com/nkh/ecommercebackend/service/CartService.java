package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.dto.request.AddItemReq;
import com.nkh.ecommercebackend.dto.request.UpdateItemQuantityReq;
import com.nkh.ecommercebackend.dto.response.CartItemRes;
import com.nkh.ecommercebackend.dto.response.CartRes;

public interface CartService {
    CartRes getCurrentCart();

    CartItemRes addItem(AddItemReq request);

    void deleteItem(String id);

    CartItemRes updateItemQuantity(String id, UpdateItemQuantityReq request);

}
