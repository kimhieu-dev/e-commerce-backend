package com.nkh.ecommercebackend.controller;

import com.nkh.ecommercebackend.dto.request.CreateCategoryReq;
import com.nkh.ecommercebackend.dto.response.BaseResponse;
import com.nkh.ecommercebackend.dto.response.CategoryRes;
import com.nkh.ecommercebackend.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public BaseResponse<CategoryRes> create(@RequestBody @Valid CreateCategoryReq request){
        CategoryRes response = categoryService.create(request);
        return BaseResponse.success(response);
    }
}
