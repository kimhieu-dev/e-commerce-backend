package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepo extends JpaRepository<OrderItem,String> {
}
