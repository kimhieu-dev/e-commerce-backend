package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.common.InventoryStatus;
import com.nkh.ecommercebackend.dto.request.AddItemReq;
import com.nkh.ecommercebackend.dto.request.UpdateItemQuantityReq;
import com.nkh.ecommercebackend.dto.response.*;
import com.nkh.ecommercebackend.entity.*;
import com.nkh.ecommercebackend.mapper.DiscountMapper;
import com.nkh.ecommercebackend.repository.*;
import com.nkh.ecommercebackend.util.CurrentUserService;
import com.nkh.ecommercebackend.exception.BusinessException;
import com.nkh.ecommercebackend.exception.ErrorCode;
import com.nkh.ecommercebackend.mapper.CartItemMapper;
import com.nkh.ecommercebackend.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepo cartRepo;
    private final CartItemRepo cartItemRepo;
    private final CartItemMapper cartItemMapper;
    private final CurrentUserService currentUserService;
    private final ProductRepo productRepo;
    private final InventoryRepo inventoryRepo;
    private final DiscountMapper discountMapper;
    private final DiscountRepo discountRepo;
    private final CarrierRepo carrierRepo;

    @Override
    public CartRes getCurrentCart() {
        User user = currentUserService.getUser();
        Cart cart = cartRepo.findByUsername(user.getUsername()).orElseThrow(() ->
                new BusinessException(ErrorCode.USER_DOES_NOT_HAVE_CART));

        List<CartItemRes> cartItemResList = cartItemMapper.toCartItemResList(cart.getCartItems());

        return CartRes.builder()
                .items(cartItemResList)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CartItemRes addItem(AddItemReq request) {
        User user = currentUserService.getUser();
        Cart cart = cartRepo.findByUsername(user.getUsername())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_DOES_NOT_HAVE_CART));

        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

        checkInventory(product);

        CartItem existingItem = cartItemRepo.findByCartIdAndProductId(cart.getId(), product.getId());

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            if (existingItem.getProduct().getInventory().getQuantityInStock() == 0) {
                throw new BusinessException(ErrorCode.PRODUCT_OUT_OF_STOCK);
            }
            if (existingItem.getQuantity() > existingItem.getProduct().getInventory().getQuantityInStock()) {
                throw new BusinessException(ErrorCode.PRODUCT_OUT_OF_RANGE);
            }
            cartItemRepo.save(existingItem);

            return cartItemMapper.toCartItemRes(existingItem);
        }

        CartItem newItem = CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(request.getQuantity())
                .checked(false)
                .build();

        if (newItem.getProduct().getInventory().getQuantityInStock() == 0) {
            throw new BusinessException(ErrorCode.PRODUCT_OUT_OF_STOCK);
        }
        if (newItem.getQuantity() > newItem.getProduct().getInventory().getQuantityInStock()) {
            throw new BusinessException(ErrorCode.PRODUCT_OUT_OF_RANGE);
        }
        cartItemRepo.save(newItem);


        return cartItemMapper.toCartItemRes(newItem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteItem(String id) {
        CartItem cartItem = cartItemRepo.findByIdAndDeletedFalse(id).orElseThrow(() -> new BusinessException(ErrorCode.CART_ITEM_NOT_FOUND));
        cartItem.setDeleted(true);
        cartItemRepo.save(cartItem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CartItemRes updateItemQuantity(String id, UpdateItemQuantityReq request) {
        User user = currentUserService.getUser();
        CartItem cartItem = cartItemRepo.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CART_ITEM_NOT_FOUND));

        if (!user.getId().equals(id)) {
            throw new BusinessException(ErrorCode.USER_DOES_NOT_HAVE_PRIVILEGE);
        }

        //trang thai inventory
        checkInventory(cartItem.getProduct());
        Product product = cartItem.getProduct();
        productRepo.save(product);

        cartItem.setQuantity(request.getQuantity());
        cartItemRepo.save(cartItem);
        return cartItemMapper.toCartItemRes(cartItem);
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
        inventoryRepo.save(inventory);
    }
}
