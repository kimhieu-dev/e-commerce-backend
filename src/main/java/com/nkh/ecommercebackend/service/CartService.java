package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.dto.request.AddItemToCartReq;
import com.nkh.ecommercebackend.dto.response.CartSummaryRes;

public interface CartService {
    CartSummaryRes getCurrentCart();

    void addItem( AddItemToCartReq request);
}
