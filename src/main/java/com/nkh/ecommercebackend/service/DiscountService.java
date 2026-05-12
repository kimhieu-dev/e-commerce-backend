package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.dto.response.DiscountRes;

import java.util.List;

public interface DiscountService {
    List<DiscountRes> getDiscounts();

    List<DiscountRes> getUsableDiscounts();
}
