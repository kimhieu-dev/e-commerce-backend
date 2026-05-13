package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.dto.response.DiscountRes;
import com.nkh.ecommercebackend.entity.Discount;
import com.nkh.ecommercebackend.mapper.DiscountMapper;
import com.nkh.ecommercebackend.repository.DiscountRepo;
import com.nkh.ecommercebackend.service.DiscountService;
import com.nkh.ecommercebackend.service.DiscountStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepo discountRepo;
    private final DiscountMapper discountMapper;

    @Override
    public List<DiscountRes> getDiscounts() {
        List<Discount> discountList = discountRepo.findAll();
        return discountMapper.toDiscountResList(discountList);
    }

    @Override
    public List<DiscountRes> getUsableDiscounts() {
        List<Discount> response = discountRepo.findAllUsableDiscounts();
        return discountMapper.toDiscountResList(response);
    }
}
