package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartItemRepo extends JpaRepository<CartItem, String> {
    CartItem findByCartIdAndProductId(String cartId, String productId);

    @Query("""
            select ci from CartItem ci where ci.cart.id = :cartId and ci.checked = true
            """)
    List<CartItem> findAllByCartIdAndCheckedTrue(String cartId);

    Optional<CartItem> findByIdAndDeletedFalse(String id);
}
