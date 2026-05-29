package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.dto.request.AddItemReq;
import com.nkh.ecommercebackend.dto.request.UpdateItemReq;
import com.nkh.ecommercebackend.dto.response.*;
import com.nkh.ecommercebackend.entity.*;
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
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepo cartRepo;
    private final CartItemRepo cartItemRepo;
    private final CartItemMapper cartItemMapper;
    private final CurrentUserService currentUserService;
    private final ProductRepo productRepo;
    private final InventoryRepo inventoryRepo;

    @Override
    public CartRes getCurrentCart() {
        User user = currentUserService.getUser();
        Cart cart = cartRepo.findByUsername(user.getUsername())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_DOES_NOT_HAVE_CART));

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
        //1. validate request: check ton tai product id : ok di tiep
        //2. check xem da co cart item voi product nay chua: co roi thi +1, chua co thi tao moi;
        //3. save xuong db;
        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
        CartItem cartItem = cartItemRepo.findByCartIdAndProductId(cart.getId(), product.getId());

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        } else {
            cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();
        }
        Inventory inventory = product.getInventory();
        if (inventory == null) {
            throw new BusinessException(ErrorCode.INVENTORY_NOT_FOUND);
        }
        int availableQuantity = inventory.getQuantityInStock() - inventory.getReservedQuantity();
        if (availableQuantity < request.getQuantity()) {
            throw new BusinessException(ErrorCode.PRODUCT_OUT_OF_RANGE);
        }
        cartItemRepo.save(cartItem);
        return cartItemMapper.toCartItemRes(cartItem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteItem(String id) {
        User user = currentUserService.getUser();
        Cart cart = cartRepo.findByUsername(user.getUsername())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_DOES_NOT_HAVE_CART));

        CartItem cartItem = cartItemRepo.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CART_ITEM_NOT_FOUND));

        if (!cart.getId().equals(cartItem.getCart().getId())) {
            throw new BusinessException(ErrorCode.USER_DOES_NOT_HAVE_PRIVILEGE);
        }
        cartItem.setDeleted(true);
        cartItemRepo.save(cartItem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CartItemRes updateItem(String id, UpdateItemReq request) {
        if (request.getQuantity() < 0) {
            throw new BusinessException(ErrorCode.QUANTITY_INVALID);
        }
        User user = currentUserService.getUser();
        Cart cart = cartRepo.findByUsername(user.getUsername())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_DOES_NOT_HAVE_CART));

        CartItem cartItem = cartItemRepo.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CART_ITEM_NOT_FOUND));
        if (!cart.getId().equals(cartItem.getCart().getId())) {
            throw new BusinessException(ErrorCode.USER_DOES_NOT_HAVE_PRIVILEGE);
        }
        if (request.getQuantity() == 0) {
            cartItem.setDeleted(true);
            cartItemRepo.save(cartItem);
        }
        int newQuantity = request.getQuantity();
        if (newQuantity > cartItem.getQuantity()) {
            Product product = cartItem.getProduct();
            Inventory inventory = product.getInventory();
            int availableQuantity = inventory.getQuantityInStock() - inventory.getReservedQuantity();

            if (availableQuantity < newQuantity) {
                throw new BusinessException(ErrorCode.PRODUCT_OUT_OF_RANGE);
            }
        }
        cartItem.setQuantity(newQuantity);
        cartItemRepo.save(cartItem);
        return cartItemMapper.toCartItemRes(cartItem);
    }

}
