package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.common.InventoryStatus;
import com.nkh.ecommercebackend.dto.request.UpdateCartItemReq;
import com.nkh.ecommercebackend.dto.response.CartItemRes;
import com.nkh.ecommercebackend.entity.*;
import com.nkh.ecommercebackend.repository.ProductRepo;
import com.nkh.ecommercebackend.util.CurrentUserService;
import com.nkh.ecommercebackend.dto.request.AddItemToCartReq;
import com.nkh.ecommercebackend.dto.response.CartRes;
import com.nkh.ecommercebackend.dto.response.CheckoutRes;
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
    private final ProductRepo productRepo;

    @Override
    public CartRes getCurrentCart() {

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

//        List<Product> productList = cartItemList.stream().map(CartItem -> CartItem.getProduct()).toList();

        List<CartItemRes> cartItemResList = cartItemMapper.toCartItemResList(cartItemList);

        CheckoutRes checkoutRes = new CheckoutRes();
        checkoutRes.setSubtotal(subtotal);
        checkoutRes.setShippingFee(BigDecimal.valueOf(30.00));
        checkoutRes.setDiscountAmount(BigDecimal.ZERO);
        checkoutRes.setTotalAmount(subtotal.add(checkoutRes.getShippingFee()).subtract(checkoutRes.getDiscountAmount()));


        CartRes cartRes = new CartRes();
        cartRes.setCartItemResList(cartItemResList);
        cartRes.setCheckoutRes(checkoutRes);

        return cartRes;
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
        checkInventory(product);
        productRepo.save(product);

        CartItem existingItem = cartItemRepo.findByCartIdAndProductId(cartOptional.get().getId(), product.getId());

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            if (existingItem.getProduct().getInventory().getQuantityInStock() == 0) {
                throw new BusinessException(ErrorCode.PRODUCT_OUT_OF_STOCK);
            }
            if(existingItem.getQuantity() > existingItem.getProduct().getInventory().getQuantityInStock()) {
                throw new BusinessException(ErrorCode.PRODUCT_OUT_OF_RANGE);
            }
            cartItemRepo.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cartOptional.get());
            newItem.setProduct(product);
            newItem.setQuantity(request.getQuantity());
            if (newItem.getProduct().getInventory().getQuantityInStock() == 0) {
                throw new BusinessException(ErrorCode.PRODUCT_OUT_OF_STOCK);
            }
            if ( newItem.getQuantity()> newItem.getProduct().getInventory().getQuantityInStock()){
                throw new BusinessException(ErrorCode.PRODUCT_OUT_OF_RANGE);
            }
            newItem.setUnitPrice(product.getBasePrice());
            cartItemRepo.save(newItem);
        }
    }

    @Override
    @Transactional
    public void deleteItem(String id) {
        CartItem cartItem = cartItemRepo.findByIdAndDeletedFalse(id).orElseThrow(() -> new BusinessException(ErrorCode.CART_ITEM_NOT_FOUND));
        cartItem.setDeleted(true);
        cartItemRepo.save(cartItem);
    }

    @Override
    @Transactional
    public CartItemRes updateQuantityItem(String id, UpdateCartItemReq request) {
        User user = currentUserService.getUser();
        CartItem cartItem = cartItemRepo.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.CART_ITEM_NOT_FOUND));

        checkInventory(cartItem.getProduct());
        Product product = cartItem.getProduct();
        productRepo.save(product);

        cartItem.setQuantity(request.getQuantity());
        cartItemRepo.save(cartItem);
        CartItemRes cartItemRes = cartItemMapper.toCartItemRes(cartItem);
        return cartItemRes;
    }

    private void checkInventory(Product product) {
        Inventory inventory = product.getInventory();
        if (inventory == null) {
            throw new BusinessException(ErrorCode.PRODUCT_DO_NOT_HAVE_INVENTORY);
        }
        if (product.getInventory().getQuantityInStock() == 0) {
            inventory.setStatus(InventoryStatus.OUT_OF_STOCK);
        } else if (product.getInventory().getQuantityInStock() <= 10) {
            inventory.setStatus(InventoryStatus.LIMITED_STOCK);
        } else {
            inventory.setStatus(InventoryStatus.IN_STOCK);
        }
    }
}
