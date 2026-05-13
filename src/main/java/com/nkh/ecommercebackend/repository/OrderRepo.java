package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderRepo extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {
}
