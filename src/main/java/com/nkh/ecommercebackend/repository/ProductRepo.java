package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepo extends JpaRepository<Product,String>, JpaSpecificationExecutor<Product> {
}
