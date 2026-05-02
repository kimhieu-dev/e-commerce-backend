package com.nkh.ecommercebackend.controller;

import com.nkh.ecommercebackend.dto.request.ProductFilterReq;
import com.nkh.ecommercebackend.dto.response.BaseResponse;
import com.nkh.ecommercebackend.dto.response.ProductRes;
import com.nkh.ecommercebackend.entity.Product;
import com.nkh.ecommercebackend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public BaseResponse<List<ProductRes>> getProducts(ProductFilterReq request, Pageable pageable) {
        List<ProductRes> response = productService.getProducts(request, pageable);
        return BaseResponse.success(response);
    }
}
