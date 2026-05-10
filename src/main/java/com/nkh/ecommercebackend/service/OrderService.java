package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.dto.request.CreateOrderReq;
import com.nkh.ecommercebackend.dto.request.OrderFilterReq;
import com.nkh.ecommercebackend.dto.response.OrderRes;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    OrderRes createOrder( CreateOrderReq request);
    List<OrderRes> getOrders(OrderFilterReq request, Pageable pageable);
}
