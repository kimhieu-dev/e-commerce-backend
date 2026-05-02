package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.dto.request.ProductFilterReq;
import com.nkh.ecommercebackend.dto.response.ProductRes;
import com.nkh.ecommercebackend.entity.Product;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Product getProductById(String id);

    List<ProductRes> getProducts(ProductFilterReq request, Pageable pageable);
}
