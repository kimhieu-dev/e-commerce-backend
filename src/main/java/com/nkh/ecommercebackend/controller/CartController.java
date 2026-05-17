package com.nkh.ecommercebackend.controller;

import com.nkh.ecommercebackend.dto.request.AddItemReq;
import com.nkh.ecommercebackend.dto.request.UpdateItemReq;
import com.nkh.ecommercebackend.dto.response.*;
import com.nkh.ecommercebackend.service.CartService;
import com.nkh.ecommercebackend.service.SummaryService;
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
    private final SummaryService summaryService;

    @GetMapping()
    public BaseResponse<CartRes> getCart() {
        CartRes response = cartService.getCurrentCart();
        return BaseResponse.success(response);
    }

    @GetMapping("/summary")
    public BaseResponse<SummaryRes> getSummary(@RequestBody String discountCode) {
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
    @PatchMapping("/items/{id}")
    public BaseResponse<CartItemRes> updateItem(@PathVariable String id, @Valid @RequestBody UpdateItemReq request) {
        CartItemRes response = cartService.updateItem(id, request);
        return BaseResponse.success(response);
    }

    @DeleteMapping("/cart/items/{id}")
    public BaseResponse<?> deleteItem(@PathVariable String id) {
        cartService.deleteItem(id);
        return BaseResponse.success("Delete item successfully");
    }

}
