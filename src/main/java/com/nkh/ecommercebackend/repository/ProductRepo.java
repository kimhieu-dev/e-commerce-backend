package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product,String> {
}
