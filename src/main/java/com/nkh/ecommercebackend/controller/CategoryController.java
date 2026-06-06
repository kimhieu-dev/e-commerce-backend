package com.nkh.ecommercebackend.controller;

import com.nkh.ecommercebackend.dto.request.CreateCategoryReq;
import com.nkh.ecommercebackend.dto.request.UpdateCategoryReq;
import com.nkh.ecommercebackend.dto.BaseResponse;
import com.nkh.ecommercebackend.dto.response.CategoryRes;
import com.nkh.ecommercebackend.service.CategoryService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping()
    public BaseResponse<List<CategoryRes>> get(){
        List<CategoryRes> response = categoryService.get();
        return BaseResponse.success(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public BaseResponse<CategoryRes> create(@RequestBody @Valid CreateCategoryReq request){
        CategoryRes response = categoryService.create(request);
        return BaseResponse.success(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping
    public BaseResponse<CategoryRes> update(@RequestBody @Valid UpdateCategoryReq request){
        CategoryRes response = categoryService.update(request);
        return BaseResponse.success(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public BaseResponse<?> delete(@PathVariable String id){
        categoryService.delete(id);
        return BaseResponse.success("Delete successfully");
    }
}
