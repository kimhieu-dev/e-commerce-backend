package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.common.InventoryStatus;
import com.nkh.ecommercebackend.dto.request.AddItemReq;
import com.nkh.ecommercebackend.dto.request.UpdateItemReq;
import com.nkh.ecommercebackend.dto.response.*;
import com.nkh.ecommercebackend.entity.*;
import com.nkh.ecommercebackend.mapper.DiscountMapper;
import com.nkh.ecommercebackend.repository.*;
import com.nkh.ecommercebackend.service.SummaryService;
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
        CartItem existingItem = cartItemRepo.findByCartIdAndProductId(cart.getId(), product.getId());
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            Inventory inventoryOfExistingItem = existingItem.getProduct().getInventory();
            int availableQuantity = inventoryOfExistingItem.getQuantityInStock() - inventoryOfExistingItem.getReservedQuantity();
            if (availableQuantity < existingItem.getQuantity()) {
                throw new BusinessException(ErrorCode.PRODUCT_OUT_OF_RANGE);
            }
            cartItemRepo.save(existingItem);
            return cartItemMapper.toCartItemRes(existingItem);
        }
        CartItem newItem = CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(1)
                .build();
        Inventory inventoryOfNewItem = newItem.getProduct().getInventory();
        int availableQuantity = inventoryOfNewItem.getQuantityInStock() - inventoryOfNewItem.getReservedQuantity();
        if (availableQuantity < newItem.getQuantity()) {
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
    public CartItemRes updateItem(String id, UpdateItemReq request) {

        //TODO REFACTOR
        User user = currentUserService.getUser();
        CartItem cartItem = cartItemRepo.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CART_ITEM_NOT_FOUND));

        if (!user.getId().equals(id)) {
            throw new BusinessException(ErrorCode.USER_DOES_NOT_HAVE_PRIVILEGE);
        }

        //trang thai inventory
        Product product = cartItem.getProduct();
        productRepo.save(product);

        cartItem.setQuantity(request.getQuantity());
        cartItemRepo.save(cartItem);
        return cartItemMapper.toCartItemRes(cartItem);
    }

}
