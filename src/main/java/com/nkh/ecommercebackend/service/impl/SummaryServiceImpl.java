package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.common.DiscountType;
import com.nkh.ecommercebackend.dto.response.SummaryRes;
import com.nkh.ecommercebackend.entity.Cart;
import com.nkh.ecommercebackend.entity.CartItem;
import com.nkh.ecommercebackend.entity.Discount;
import com.nkh.ecommercebackend.entity.User;
import com.nkh.ecommercebackend.exception.BusinessException;
import com.nkh.ecommercebackend.exception.ErrorCode;
import com.nkh.ecommercebackend.repository.CartItemRepo;
import com.nkh.ecommercebackend.repository.CartRepo;
import com.nkh.ecommercebackend.repository.DiscountRepo;
import com.nkh.ecommercebackend.service.DiscountStrategy;
import com.nkh.ecommercebackend.service.SummaryService;
import com.nkh.ecommercebackend.service.factory.DiscountStrategyFactory;
import com.nkh.ecommercebackend.util.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SummaryServiceImpl implements SummaryService {
    private final CartItemRepo cartItemRepo;
    private final DiscountStrategyFactory discountStrategyFactory;

    @Override
    public SummaryRes getSummary(Cart cart, Discount discount) {

        List<CartItem> cartItemList = cartItemRepo.findAllByCartIdAndCheckedTrueWithProduct(cart.getId());

        BigDecimal subtotal = cartItemList.stream()
                .map(item -> item
                        .getProduct()
                        .getBasePrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal shippingFee = BigDecimal.valueOf(30.00);

        DiscountStrategy strategy = discountStrategyFactory.create(discount.getType(), discount.getValue());

        BigDecimal discountValue = strategy.calculate(subtotal);

        BigDecimal totalAmount = subtotal.add(shippingFee).subtract(discountValue);

        return SummaryRes.builder()
                .subtotal(subtotal)
                .shippingFee(shippingFee)
                .discountAmount(discountValue)
                .totalAmount(totalAmount)
                .build();
    }
}
