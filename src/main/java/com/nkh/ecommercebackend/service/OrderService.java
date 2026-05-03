package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.dto.request.CreateOrderReq;
import com.nkh.ecommercebackend.dto.response.OrderRes;

public interface OrderService {
    OrderRes createOrder( CreateOrderReq request);
}
