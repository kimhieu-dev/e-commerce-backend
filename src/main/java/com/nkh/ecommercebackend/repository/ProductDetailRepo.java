package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDetailRepo extends JpaRepository<ProductDetail, String> {
}
