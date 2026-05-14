package com.nkh.ecommercebackend.controller;

import com.nkh.ecommercebackend.dto.request.CreateProductReq;
import com.nkh.ecommercebackend.dto.request.ProductFilterReq;
import com.nkh.ecommercebackend.dto.request.UpdateProductReq;
import com.nkh.ecommercebackend.dto.response.BaseResponse;
import com.nkh.ecommercebackend.dto.response.ProductOverviewRes;
import com.nkh.ecommercebackend.dto.response.ProductRes;
import com.nkh.ecommercebackend.entity.Product;
import com.nkh.ecommercebackend.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public BaseResponse<List<ProductRes>> getProducts(ProductFilterReq request, @PageableDefault(size = 4, page = 0) Pageable pageable) {
        List<ProductRes> response = productService.getProducts(request, pageable);
        return BaseResponse.success(response);
    }

    @PostMapping
    public BaseResponse<ProductRes> createProduct(@RequestBody @Valid CreateProductReq request) {
        ProductRes response = productService.createProduct(request);
        return BaseResponse.success(response);
    }

    @GetMapping("/overview")
    public BaseResponse<ProductOverviewRes> getOverview(@RequestParam(value = "fromDate", required = false) LocalDate fromDate,
                                                        @RequestParam(value = "toDate", required = false) LocalDate toDate) {
        ProductOverviewRes response = productService.getOverview(fromDate,toDate);
        return BaseResponse.success(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public BaseResponse<ProductRes> updateProduct(@PathVariable String id, @RequestBody @Valid UpdateProductReq request) {
        ProductRes response = productService.updateProduct(id,request);
        return BaseResponse.success(response);
    }
}
