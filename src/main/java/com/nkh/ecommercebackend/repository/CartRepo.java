package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.entity.Cart;
import com.nkh.ecommercebackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart,String> {

    Boolean existsByUserId(String userId);

    Optional<Cart> findByUserId(String userId);

    String user(User user);
}
