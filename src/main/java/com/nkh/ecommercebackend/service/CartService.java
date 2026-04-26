package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.entity.Cart;
import com.nkh.ecommercebackend.entity.User;

public interface CartService {
    Cart getCart(String userId);
}
