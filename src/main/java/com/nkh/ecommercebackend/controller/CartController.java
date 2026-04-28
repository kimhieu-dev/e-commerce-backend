package com.nkh.ecommercebackend.controller;

import com.nkh.ecommercebackend.dto.request.AddItemToCartReq;
import com.nkh.ecommercebackend.dto.response.CartSummaryRes;
import com.nkh.ecommercebackend.entity.User;
import com.nkh.ecommercebackend.mapper.CartMapper;
import com.nkh.ecommercebackend.service.CartService;
import com.nkh.ecommercebackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final UserService userService;
    private final CartMapper cartMapper;

    @GetMapping
    public ResponseEntity<CartSummaryRes> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByUsername(userDetails.getUsername());
        CartSummaryRes response = cartService.getCartSummary(user.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/items")
    public ResponseEntity<Void> addItem(@AuthenticationPrincipal UserDetails userDetails, @RequestBody AddItemToCartReq request) {
        User user = userService.getUserByUsername(userDetails.getUsername());
        cartService.addItem(user.getId(), request);
        return ResponseEntity.ok().build();
    }


}
