package com.nkh.ecommercebackend.controller;

import com.nkh.ecommercebackend.dto.request.AddItemReq;
import com.nkh.ecommercebackend.dto.request.UpdateCartItemReq;
import com.nkh.ecommercebackend.dto.response.*;
import com.nkh.ecommercebackend.service.CartService;
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

    @GetMapping()
    public BaseResponse<CartRes> getCart() {
        CartRes response = cartService.getCurrentCart();
        return BaseResponse.success(response);
    }

    @GetMapping("/summary")
    public BaseResponse<SummaryRes> getSummary(@RequestBody  String discountCode) {
        SummaryRes response = cartService.getSummary(discountCode);
        return BaseResponse.success(response);
    }

    @PostMapping()
    public BaseResponse<CartItemRes> addItem(@RequestBody @Valid AddItemReq request) {
        CartItemRes response = cartService.addItem(request);
        return BaseResponse.success(response);
    }

    ///
    /// sửa đoạn này để trả về cart, sau khi tăng giảm số lượng thì load lại card
    ///
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

}
