package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.dto.response.DiscountRes;
import com.nkh.ecommercebackend.entity.Discount;
import com.nkh.ecommercebackend.mapper.DiscountMapper;
import com.nkh.ecommercebackend.repository.DiscountRepo;
import com.nkh.ecommercebackend.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepo discountRepo;
    private final DiscountMapper discountMapper;

    @Override
    public List<DiscountRes> getDiscounts() {
        List<Discount> discountList = discountRepo.findAll();
        List<DiscountRes> discountResList = discountMapper.toDiscountResList(discountList);
        return discountResList;
    }
}
