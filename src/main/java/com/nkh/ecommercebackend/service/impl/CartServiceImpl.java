package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.dto.response.CartItemRes;
import com.nkh.ecommercebackend.util.CurrentUserService;
import com.nkh.ecommercebackend.dto.request.AddItemToCartReq;
import com.nkh.ecommercebackend.dto.response.CartSummaryRes;
import com.nkh.ecommercebackend.dto.response.CheckoutRes;
import com.nkh.ecommercebackend.entity.Cart;
import com.nkh.ecommercebackend.entity.CartItem;
import com.nkh.ecommercebackend.entity.Product;
import com.nkh.ecommercebackend.entity.User;
import com.nkh.ecommercebackend.exception.BusinessException;
import com.nkh.ecommercebackend.exception.ErrorCode;
import com.nkh.ecommercebackend.mapper.CartItemMapper;
import com.nkh.ecommercebackend.mapper.CartMapper;
import com.nkh.ecommercebackend.repository.CartItemRepo;
import com.nkh.ecommercebackend.repository.CartRepo;
import com.nkh.ecommercebackend.repository.UserRepo;
import com.nkh.ecommercebackend.service.CartService;
import com.nkh.ecommercebackend.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepo cartRepo;
    private final CartItemRepo cartItemRepo;
    private final ProductService productService;
    private final CartItemMapper cartItemMapper;
    private final CartMapper cartMapper;
    private final UserRepo userRepo;
    private final CurrentUserService currentUserService;

    @Override
    public CartSummaryRes getCurrentCart() {

        User user = currentUserService.getUser();

        Optional<Cart> cartOptional = cartRepo.findByUserId(user.getId());
        if (cartOptional.isEmpty()) {
            throw new BusinessException(ErrorCode.USER_DOES_NOT_HAVE_CART);
        }
        List<CartItem> cartItemList = cartItemRepo.findAllByCartId(cartOptional.get().getId());
        BigDecimal subtotal = BigDecimal.ZERO;
        for (CartItem cartItem : cartItemList) {
            subtotal = subtotal.add(cartItem.getProduct().getBasePrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        List<CartItemRes> cartItemResList = cartItemMapper.toCartItemResList(cartItemList);

        CheckoutRes checkoutRes = new CheckoutRes();
        checkoutRes.setSubtotal(subtotal);
        checkoutRes.setShippingFee(BigDecimal.valueOf(30.00));
        checkoutRes.setDiscountAmount(BigDecimal.ZERO);
        checkoutRes.setTotalAmount(subtotal.add(checkoutRes.getShippingFee()).subtract(checkoutRes.getDiscountAmount()));


        CartSummaryRes cartSummaryRes = new CartSummaryRes();
        cartSummaryRes.setItems(cartItemResList);
        cartSummaryRes.setSummary(checkoutRes);

        return cartSummaryRes;
    }

    @Override
    @Transactional
    public void addItem(AddItemToCartReq request) {

        User user = currentUserService.getUser();

        Optional<Cart> cartOptional = cartRepo.findByUserId(user.getId());
        if (cartOptional.isEmpty()) {
            throw new BusinessException(ErrorCode.USER_DOES_NOT_HAVE_CART);
        }

        Product product = productService.getProductById(request.getProductId());

        CartItem existingItem = cartItemRepo.findByCartIdAndProductId(cartOptional.get().getId(), product.getId());

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            cartItemRepo.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cartOptional.get());
            newItem.setProduct(product);
            newItem.setQuantity(request.getQuantity());
            newItem.setUnitPrice(product.getBasePrice());
            cartItemRepo.save(newItem);
        }

    }


}
