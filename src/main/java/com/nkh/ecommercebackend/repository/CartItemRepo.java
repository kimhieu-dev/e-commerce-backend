package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CartItemRepo extends JpaRepository<CartItem, String> {
    CartItem findByCartIdAndProductId(String cartId, String productId);
    Optional<CartItem> findByIdAndDeletedFalse(String id);
    List<CartItem> findAllByCartIdAndDeletedFalse(String cartId);

    @Modifying
    @Query(""" 
            update CartItem c
            set c.deleted = true
            where c.cart.id = :cartId
            and c.product.id in :productIds
            """)
    void softDeletePurchasedItems(String id, Set<String> purchasedProductIds);
}
