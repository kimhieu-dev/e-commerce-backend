package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.entity.Cart;
import com.nkh.ecommercebackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, String> {

    Boolean existsByUserId(String userId);

    @Query(value = """
                select c from Cart c
                join fetch c.cartItems ci
                join fetch ci.product p
                join fetch p.inventory
                join fetch p.productDetail
                where c.user.username = :username
            """)
    Optional<Cart> findByUsername(String username);

}
