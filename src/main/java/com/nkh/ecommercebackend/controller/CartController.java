package com.nkh.ecommercebackend.controller;

import com.nkh.ecommercebackend.dto.request.AddItemToCartReq;
import com.nkh.ecommercebackend.dto.request.UpdateCartItemReq;
import com.nkh.ecommercebackend.dto.response.BaseResponse;
import com.nkh.ecommercebackend.dto.response.CartItemRes;
import com.nkh.ecommercebackend.dto.response.CartRes;
import com.nkh.ecommercebackend.mapper.CartMapper;
import com.nkh.ecommercebackend.service.CartService;
import com.nkh.ecommercebackend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Validated
public class CartController {
    private final CartService cartService;
    private final UserService userService;
    private final CartMapper cartMapper;

    @GetMapping
    public BaseResponse<CartRes> getCart() {
        CartRes response = cartService.getCurrentCart();
        return BaseResponse.success(response);
    }

    @PostMapping("/items")
    public BaseResponse<?> addItem(@RequestBody AddItemToCartReq request) {
        cartService.addItem(request);
        return BaseResponse.success("Add item successfully");
    }

    @PatchMapping("/items/{id}")
    public BaseResponse<CartItemRes> updateQuantityItem(@PathVariable String id, @Valid @RequestBody UpdateCartItemReq request) {
        CartItemRes response = cartService.updateQuantityItem(id,request);
        return BaseResponse.success(response);
    }

    @DeleteMapping("/items/{id}")
    public BaseResponse<?> deleteItem(@PathVariable String id) {
        cartService.deleteItem(id);
        return BaseResponse.success("Delete item successfully");
    }
}
