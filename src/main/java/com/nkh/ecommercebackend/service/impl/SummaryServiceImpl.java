package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.dto.response.OrderSummary;
import com.nkh.ecommercebackend.entity.Cart;
import com.nkh.ecommercebackend.entity.CartItem;
import com.nkh.ecommercebackend.entity.Discount;
import com.nkh.ecommercebackend.entity.Product;
import com.nkh.ecommercebackend.exception.BusinessException;
import com.nkh.ecommercebackend.exception.ErrorCode;
import com.nkh.ecommercebackend.repository.CartItemRepo;
import com.nkh.ecommercebackend.repository.DiscountRepo;
import com.nkh.ecommercebackend.repository.ProductRepo;
import com.nkh.ecommercebackend.service.DiscountStrategy;
import com.nkh.ecommercebackend.service.SummaryService;
import com.nkh.ecommercebackend.service.factory.DiscountStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SummaryServiceImpl implements SummaryService {
    private final CartItemRepo cartItemRepo;
    private final DiscountStrategyFactory discountStrategyFactory;
    private final ProductRepo productRepo;
    private final DiscountRepo discountRepo;

    @Override
    public OrderSummary getSummary(Map<String, Integer> productQuantityMap, String discountCode) {

        List<Product> products = productRepo.findAllById(productQuantityMap.keySet());
        if (products.size() != productQuantityMap.size()) {
            throw new BusinessException(ErrorCode.SOME_PRODUCT_NOT_EXIST);
        }

        Discount discount = discountRepo.findByCode(discountCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.DISCOUNT_NOT_FOUND));

        BigDecimal subtotal = products.stream().map(
                product -> product.getBasePrice()
                        .multiply(
                                BigDecimal.valueOf(
                                        productQuantityMap.get(product.getId())
                                )
                        )
        ).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal shippingFee = BigDecimal.valueOf(30.00);

        DiscountStrategy strategy = discountStrategyFactory.create(discount.getType(), discount.getValue());

        BigDecimal discountAmount = strategy.calculate(subtotal);

        BigDecimal totalAmount = subtotal.add(shippingFee).subtract(discountAmount);

        return OrderSummary.builder()
                .subtotal(subtotal)
                .shippingFee(shippingFee)
                .discountAmount(discountAmount)
                .totalAmount(totalAmount)
                .build();
    }
}
