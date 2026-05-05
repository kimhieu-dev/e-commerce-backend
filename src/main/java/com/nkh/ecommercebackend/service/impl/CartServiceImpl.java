package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.common.DiscountType;
import com.nkh.ecommercebackend.common.InventoryStatus;
import com.nkh.ecommercebackend.dto.request.AddItemReq;
import com.nkh.ecommercebackend.dto.request.UpdateCartItemReq;
import com.nkh.ecommercebackend.dto.response.*;
import com.nkh.ecommercebackend.entity.*;
import com.nkh.ecommercebackend.mapper.DiscountMapper;
import com.nkh.ecommercebackend.repository.*;
import com.nkh.ecommercebackend.util.CurrentUserService;
import com.nkh.ecommercebackend.exception.BusinessException;
import com.nkh.ecommercebackend.exception.ErrorCode;
import com.nkh.ecommercebackend.mapper.CartItemMapper;
import com.nkh.ecommercebackend.service.CartService;
import com.nkh.ecommercebackend.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepo cartRepo;
    private final CartItemRepo cartItemRepo;
    private final ProductService productService;
    private final CartItemMapper cartItemMapper;
    private final CurrentUserService currentUserService;
    private final ProductRepo productRepo;
    private final DiscountMapper discountMapper;
    private final DiscountRepo discountRepo;
    private final CarrierRepo carrierRepo;

    @Override
    public CartRes getCurrentCart() {
        User user = currentUserService.getUser();
        Cart cart = cartRepo.findByUsername(user.getUsername()).orElseThrow(() ->
                new BusinessException(ErrorCode.USER_DOES_NOT_HAVE_CART));

        List<CartItemRes> cartItemResList = cartItemMapper.toCartItemResList(cart.getCartItems());

        CartRes cartRes = CartRes.builder()
                .items(cartItemResList)
                .build();
        return cartRes;
    }

    @Override
    @Transactional
    public CartItemRes addItem(AddItemReq request) {
        User user = currentUserService.getUser();
        Cart cart = cartRepo.findByUsername(user.getUsername()).orElseThrow(() ->
                new BusinessException(ErrorCode.USER_DOES_NOT_HAVE_CART));

        Product product = productService.getProductById(request.getProductId());
        checkInventory(product);
        productRepo.save(product);

        //dung list, set, map lay ra item tuong ung
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
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(request.getQuantity());
            if (newItem.getProduct().getInventory().getQuantityInStock() == 0) {
                throw new BusinessException(ErrorCode.PRODUCT_OUT_OF_STOCK);
            }
            if (newItem.getQuantity() > newItem.getProduct().getInventory().getQuantityInStock()) {
                throw new BusinessException(ErrorCode.PRODUCT_OUT_OF_RANGE);
            }
            cartItemRepo.save(newItem);
        }

        return CartItemRes.builder()
                .id(null)
                .product(null)
                .quantity(5)
                .build();
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


        //trang thai inventory
        checkInventory(cartItem.getProduct());
        Product product = cartItem.getProduct();
        productRepo.save(product);

        cartItem.setQuantity(request.getQuantity());
        cartItemRepo.save(cartItem);
        CartItemRes cartItemRes = cartItemMapper.toCartItemRes(cartItem);
        return cartItemRes;
    }
//
//    @Override
//    public CartRes appyDiscount(String id, DiscountReq request) {
//        User user = currentUserService.getUser();
//        Optional<Cart> cartOptional = cartRepo.findByUsername(user.getUsername());
//        if (cartOptional.isEmpty()) {
//            throw new BusinessException(ErrorCode.USER_DOES_NOT_HAVE_CART);
//        }
//
//        Optional<Discount> discount = discountRepo.findByCode(request.getCode());
//        if (discount.isEmpty()) {
//            throw new BusinessException(ErrorCode.DISCOUNT_NOT_FOUND);
//        }
//
//        if (discount.get().getEndDate().isBefore(LocalDate.now())) {
//            throw new BusinessException(ErrorCode.DISCOUNT_EXPIRED);
//        }
//
//        List<CartItem> cartItemList = cartItemRepo.findAllByCartId(cartOptional.get().getId());
//        BigDecimal subtotal = BigDecimal.ZERO;
//        for (CartItem cartItem : cartItemList) {
//            subtotal = subtotal.add(cartItem.getProduct().getBasePrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
//        }
//
//        List<CartItemRes> cartItemResList = cartItemMapper.toCartItemResList(cartItemList);
//
//        CheckoutRes checkoutRes = new CheckoutRes();
//        checkoutRes.setSubtotal(subtotal);
//        checkoutRes.setShippingFee(BigDecimal.valueOf(30.00));
//
//        if (discount.get().getType() == DiscountType.FIXED_AMOUNT) {
//            checkoutRes.setDiscountAmount(discount.get().getValue());
//        } else if (discount.get().getType() == DiscountType.PERCENTAGE) {
//            checkoutRes.setDiscountAmount(discount.get().getValue().multiply(subtotal).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
//        } else {
//            checkoutRes.setDiscountAmount(BigDecimal.ZERO);
//        }
//
//        checkoutRes.setTotalAmount(subtotal.add(checkoutRes.getShippingFee()).subtract(checkoutRes.getDiscountAmount()));
//
//        List<Discount> discountList = discountRepo.findAll();
//        List<DiscountRes> discountResList = discountMapper.toDiscountResList(discountList);
//
//        CartRes cartRes = new CartRes();
//        cartRes.setItems(cartItemResList);
//        cartRes.setCheckoutRes(checkoutRes);
//        cartRes.setDiscountsList(discountResList);

//        return cartRes;
//    }

    @Override
    public SummaryRes getSummary(String discountCode) {
        User user = currentUserService.getUser();
        Cart cart = cartRepo.findByUsername(user.getUsername()).orElseThrow(() -> new BusinessException(ErrorCode.USER_DOES_NOT_HAVE_CART));

        List<CartItem> cartItemList = cartItemRepo.findAllByCartIdAndCheckedTrue(cart.getId());

        BigDecimal subtotal = cartItemList.stream()
                .map(item -> item
                        .getProduct()
                        .getBasePrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal shippingFee = BigDecimal.valueOf(30.00);

        BigDecimal discountAmount;
        Discount discount = discountRepo.findByCode(discountCode).orElseThrow(() -> new BusinessException(ErrorCode.DISCOUNT_NOT_FOUND));
        if (discount.getType() == DiscountType.PERCENTAGE) {
            discountAmount = discount.getValue().multiply(subtotal).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
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
