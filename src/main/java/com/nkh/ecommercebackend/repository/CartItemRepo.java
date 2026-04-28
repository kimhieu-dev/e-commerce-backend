package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepo extends JpaRepository<CartItem, String> {
    CartItem findByCartIdAndProductId(String cartId, String productId);

    List<CartItem> findAllByCartId(String cartId);
}
