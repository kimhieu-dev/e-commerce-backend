package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.entity.Cart;
import com.nkh.ecommercebackend.repository.CartRepo;
import com.nkh.ecommercebackend.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepo cartRepo;

    @Override
    public Cart getCart(String userId) {
        return null;
    }
}
