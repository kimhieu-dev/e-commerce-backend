package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepo extends JpaRepository<Cart,Integer> {

}
