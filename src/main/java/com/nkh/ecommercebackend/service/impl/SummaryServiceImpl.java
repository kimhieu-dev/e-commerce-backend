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
import com.nkh.ecommercebackend.service.CartService;
import com.nkh.ecommercebackend.service.SummaryService;
import com.nkh.ecommercebackend.util.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SummaryServiceImpl implements SummaryService {
    private final CartRepo cartRepo;
    private final DiscountRepo discountRepo;
    private final CurrentUserService currentUserService;
    private final CartItemRepo cartItemRepo;

    @Override
    public SummaryRes getSummary(String discountCode) {
        User user = currentUserService.getUser();
        Cart cart = cartRepo.findByUsername(user.getUsername())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_DOES_NOT_HAVE_CART));

        List<CartItem> cartItemList = cartItemRepo.findAllByCartIdAndCheckedTrue(cart.getId());

        BigDecimal subtotal = cartItemList.stream()
                .map(item -> item
                        .getProduct()
                        .getBasePrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal shippingFee = BigDecimal.valueOf(30.00);

        BigDecimal discountAmount;
        Discount discount = discountRepo.findByCode(discountCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.DISCOUNT_NOT_FOUND));

        if (discount.getType() == DiscountType.PERCENTAGE) {
            discountAmount = discount.getValue()
                    .multiply(subtotal)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else {
            discountAmount = discount.getValue();
        }

        BigDecimal totalAmount = subtotal.add(shippingFee).subtract(discountAmount);

        return SummaryRes.builder()
                .subtotal(subtotal)
                .shippingFee(shippingFee)
                .discountAmount(discountAmount)
                .totalAmount(totalAmount)
                .build();
    }
}
