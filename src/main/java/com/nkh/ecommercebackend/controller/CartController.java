package com.nkh.ecommercebackend.controller;

import com.nkh.ecommercebackend.dto.request.AddItemToCartReq;
import com.nkh.ecommercebackend.dto.request.DiscountReq;
import com.nkh.ecommercebackend.dto.request.UpdateCartItemReq;
import com.nkh.ecommercebackend.dto.response.BaseResponse;
import com.nkh.ecommercebackend.dto.response.CartItemRes;
import com.nkh.ecommercebackend.dto.response.CartRes;
import com.nkh.ecommercebackend.dto.response.DiscountRes;
import com.nkh.ecommercebackend.mapper.CartMapper;
import com.nkh.ecommercebackend.service.CartService;
import com.nkh.ecommercebackend.service.DiscountService;
import com.nkh.ecommercebackend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@RestController
@RequestMapping("/api/v1/checkout")
@RequiredArgsConstructor
@Validated
public class CartController {
    private final CartService cartService;
    private final DiscountService discountService;

    @GetMapping("/cart")
    public BaseResponse<CartRes> getCart() {
        CartRes response = cartService.getCurrentCart();
        return BaseResponse.success(response);
    }

    @PostMapping("/cart/items")
    public BaseResponse<?> addItem(@RequestBody @Valid AddItemToCartReq request) {
        cartService.addItem(request);
        return BaseResponse.success("Add item successfully");
    }

    @PatchMapping("/cart/items/{id}")
    public BaseResponse<CartItemRes> updateQuantityItem(@PathVariable String id, @Valid @RequestBody UpdateCartItemReq request) {
        CartItemRes response = cartService.updateQuantityItem(id, request);
        return BaseResponse.success(response);
    }

    @DeleteMapping("/cart/items/{id}")
    public BaseResponse<?> deleteItem(@PathVariable String id) {
        cartService.deleteItem(id);
        return BaseResponse.success("Delete item successfully");
    }

    @PatchMapping("/cart/{id}")
    public BaseResponse<CartRes> applyDiscount(@PathVariable String id, @RequestBody @Valid DiscountReq request) {
        CartRes response = cartService.appyDiscount(id, request);
        return BaseResponse.success(response);
    }
}
