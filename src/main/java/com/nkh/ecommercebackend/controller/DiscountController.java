package com.nkh.ecommercebackend.controller;

import com.nkh.ecommercebackend.dto.response.BaseResponse;
import com.nkh.ecommercebackend.dto.response.DiscountRes;
import com.nkh.ecommercebackend.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/discounts")
@RequiredArgsConstructor
@Validated
public class DiscountController {
    private final DiscountService discountService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/all")
    public BaseResponse<List<DiscountRes>> getDiscounts(){
        List<DiscountRes> response = discountService.getDiscounts();
        return BaseResponse.success(response);
    }

    @GetMapping()
    public BaseResponse<List<DiscountRes>> getUsableDiscounts(){
        List<DiscountRes> response = discountService.getUsableDiscounts();
        return BaseResponse.success(response);
    }
}
