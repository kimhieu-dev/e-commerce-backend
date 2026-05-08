package com.nkh.ecommercebackend.service.factory;

import com.nkh.ecommercebackend.common.OrderStatus;
import com.nkh.ecommercebackend.common.PaymentMethod;
import com.nkh.ecommercebackend.common.PaymentStatus;
import com.nkh.ecommercebackend.dto.response.OrderRes;
import com.nkh.ecommercebackend.dto.response.SummaryRes;
import com.nkh.ecommercebackend.entity.*;
import com.nkh.ecommercebackend.exception.BusinessException;
import com.nkh.ecommercebackend.exception.ErrorCode;
import com.nkh.ecommercebackend.mapper.OrderMapper;
import com.nkh.ecommercebackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderFactory {
    private final OrderRepo orderRepo;
    private final CartItemRepo cartItemRepo;
    private final InventoryRepo inventoryRepo;
    private final OrderItemRepo orderItemRepo;
    private final DiscountRepo discountRepo;
    private final OrderMapper orderMapper;

    public OrderRes generateOrder(String trackingNumber, User user, Cart cart, Discount discount, Carrier carrier, Address address, PaymentMethod paymentMethod, SummaryRes summary) {

        //dung strategy pattern thay the if-else
        PaymentStatus paymentStatus;
        if (paymentMethod == PaymentMethod.COD) {
            paymentStatus = PaymentStatus.UNPAID;
        } else {
            paymentStatus = PaymentStatus.AWAITING_PAYMENT;
        }

        Order order = Order.builder()
                .trackingNumber(trackingNumber)
                .user(user)
                .paymentMethod(paymentMethod)
                .status(OrderStatus.PENDING)
                .paymentStatus(paymentStatus)
                .totalPrice(summary.getSubtotal())
                .shippingFee(summary.getShippingFee())
                .discountAmount(summary.getDiscountAmount())
                .grandTotal(summary.getTotalAmount())
                .discount(discount)
                .estimatedDelivery(LocalDate.now().plusDays(carrier.getEstimatedDays()))
                .carrier(carrier)
                .address(address)
                .build();
        orderRepo.save(order);

        List<CartItem> cartItemList = cartItemRepo.findAllByCartIdAndCheckedTrueWithProduct(cart.getId());
        if (cartItemList.isEmpty()) {
            throw new BusinessException(ErrorCode.CART_IS_EMPTY);
        }

        List<OrderItem> orderItemList = new ArrayList<>();
        List<Inventory> inventories = new ArrayList<>();

        for (CartItem cartItem : cartItemList) {
            Product product = cartItem.getProduct();

            Inventory inventory = product.getInventory();
            if (inventory.getQuantityInStock() < cartItem.getQuantity()) {
                throw new BusinessException(ErrorCode.PRODUCT_OUT_OF_STOCK);
            }
            inventory.setQuantityInStock(inventory.getQuantityInStock() - cartItem.getQuantity());
            inventories.add(inventory);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .price(product.getBasePrice())
                    .build();
            cartItem.setChecked(false);
            cartItem.setDeleted(true);
            orderItemList.add(orderItem);
        }

        inventoryRepo.saveAll(inventories);
        orderItemRepo.saveAll(orderItemList);
        order.setOrderItems(orderItemList);

        cartItemRepo.saveAll(cartItemList);

        int updated = discountRepo.increaseUserDiscount(discount.getId());
        if (updated == 0) {
            throw new BusinessException(ErrorCode.DISCOUNT_EXCEED);
        }
        return orderMapper.toOrderRes(order);
    }
}
