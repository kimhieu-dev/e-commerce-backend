//package com.nkh.ecommercebackend.controller;
//
//import com.nkh.ecommercebackend.entity.Cart;
//import com.nkh.ecommercebackend.entity.CartItem;
//import com.nkh.ecommercebackend.service.CartService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/cart")
//@RequiredArgsConstructor
//public class CartController {
//    private final CartService cartService;
//
//    @GetMapping
//    public ResponseEntity<List<CartItem>> getCart(String userId) {
//        cartService.getCart(userId);
//    }
//}
