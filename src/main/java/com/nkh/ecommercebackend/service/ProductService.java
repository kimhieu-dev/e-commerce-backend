package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.dto.request.CreateProductReq;
import com.nkh.ecommercebackend.dto.request.ProductFilterReq;
import com.nkh.ecommercebackend.dto.request.UpdateProductReq;
import com.nkh.ecommercebackend.dto.response.ProductOverviewRes;
import com.nkh.ecommercebackend.dto.response.ProductRes;
import com.nkh.ecommercebackend.entity.Product;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ProductService {
    Product getProductById(String id);

    List<ProductRes> getProducts(ProductFilterReq request, Pageable pageable);

    ProductRes createProduct(CreateProductReq request);

    ProductOverviewRes getOverview(LocalDate fromDate, LocalDate toDate);

    ProductRes updateProduct(String id, UpdateProductReq request);
}
