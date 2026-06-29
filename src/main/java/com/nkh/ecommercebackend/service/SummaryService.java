package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.dto.response.OrderSummary;
import com.nkh.ecommercebackend.entity.Cart;
import com.nkh.ecommercebackend.entity.Discount;
import com.nkh.ecommercebackend.entity.Product;

import java.util.List;
import java.util.Map;

public interface SummaryService {
    OrderSummary getSummary(Map<String,Integer> productQuantityMap, String discountCode, List<Product> products, Discount discount);
    OrderSummary getSummary(Map<String,Integer> productQuantityMap, String discountCode);

}
