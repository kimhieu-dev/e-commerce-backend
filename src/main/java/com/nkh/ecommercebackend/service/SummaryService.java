package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.dto.response.OrderSummary;
import com.nkh.ecommercebackend.entity.Cart;
import com.nkh.ecommercebackend.entity.Discount;

import java.util.Map;

public interface SummaryService {
    OrderSummary getSummary(Map<String,Integer> productQuantityMap,String discountCode);
}
