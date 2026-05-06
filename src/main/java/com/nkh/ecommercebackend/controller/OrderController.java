package com.nkh.ecommercebackend.controller;

import com.nkh.ecommercebackend.dto.request.CreateOrderReq;
import com.nkh.ecommercebackend.dto.response.BaseResponse;
import com.nkh.ecommercebackend.dto.response.OrderRes;
import com.nkh.ecommercebackend.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public BaseResponse<OrderRes> createOrder(@RequestBody @Valid CreateOrderReq request) {
        OrderRes response = orderService.createOrder(request);
        return BaseResponse.success(response);
    }
}
