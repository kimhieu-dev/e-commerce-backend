package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.dto.request.AddItemReq;
import com.nkh.ecommercebackend.dto.request.UpdateItemReq;
import com.nkh.ecommercebackend.dto.response.CartItemRes;
import com.nkh.ecommercebackend.dto.response.CartRes;
import com.nkh.ecommercebackend.dto.response.SummaryRes;

public interface CartService {
    CartRes getCurrentCart();

    CartItemRes addItem(AddItemReq request);

    void deleteItem(String id);

    CartItemRes updateItem(String id, UpdateItemReq request);

    SummaryRes getSummary(String discountCode);
}
