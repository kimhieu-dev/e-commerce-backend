package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.common.DiscountType;
import com.nkh.ecommercebackend.common.OrderStatus;
import com.nkh.ecommercebackend.common.PaymentMethod;
import com.nkh.ecommercebackend.common.PaymentStatus;
import com.nkh.ecommercebackend.dto.request.CreateOrderReq;
import com.nkh.ecommercebackend.dto.response.OrderRes;
import com.nkh.ecommercebackend.entity.*;
import com.nkh.ecommercebackend.exception.BusinessException;
import com.nkh.ecommercebackend.exception.ErrorCode;
import com.nkh.ecommercebackend.repository.*;
import com.nkh.ecommercebackend.service.OrderService;
import com.nkh.ecommercebackend.util.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepo orderRepo;
    private final CurrentUserService currentUserService;
    private final DiscountRepo discountRepo;
    private final CarrierRepo carrierRepo;
    private final AddressRepo addressRepo;

    @Override
    public OrderRes createOrder(CreateOrderReq request) {
        User user = currentUserService.getUser();
        Optional<Discount> discountOptional = discountRepo.findByCode(request.getDiscountCode());
        if (discountOptional.isEmpty()) {
            throw new BusinessException(ErrorCode.DISCOUNT_NOT_FOUND);
        }
        Optional<Carrier> carrierOptional = carrierRepo.findById(request.getCarrierId());
        if (carrierOptional.isEmpty()) {
            throw new BusinessException(ErrorCode.CARRIER_NOT_FOUND);
        }
        Optional<Address> addressOptional = addressRepo.findById(request.getAddressId());
        if (addressOptional.isEmpty()) {
            throw new BusinessException(ErrorCode.ADDRESS_NOT_FOUND);
        }
        List<CartItem> cartItemList = user.getCart().getCartItems();
        BigDecimal subtotal = BigDecimal.ZERO;
        for (CartItem cartItem : cartItemList) {
            subtotal = subtotal.add(cartItem.getProduct().getBasePrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        PaymentStatus paymentStatus;
        if (request.getPaymentMethod() == PaymentMethod.COD) {
            paymentStatus = PaymentStatus.UNPAID;
        } else {
            paymentStatus = PaymentStatus.AWAITING_PAYMENT;
        }

        BigDecimal discountAmount = BigDecimal.ZERO;
        if (discountOptional.get().getType() == DiscountType.FIXED_AMOUNT) {
            discountAmount = discountOptional.get().getValue();
        } else if (discountOptional.get().getType() == DiscountType.PERCENTAGE) {
            discountAmount = discountOptional.get().getValue().multiply(subtotal).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else {
            discountAmount = BigDecimal.ZERO;
        }

        BigDecimal grandTotal = subtotal.add(BigDecimal.valueOf(30.00).subtract(discountAmount));

        Order order = Order.builder()
                .trackingNumber(generateTrackingNumber("TEST"))
                .user(user)
                .paymentMethod(request.getPaymentMethod())
                .status(OrderStatus.PENDING)
                .paymentStatus(paymentStatus)
                .totalPrice(subtotal)
                .shippingFee(BigDecimal.valueOf(30.00))
                .discountAmount(discountAmount)
                .grandTotal(grandTotal)
                .discount(discountOptional.get())
                //.estimatedDelivery()
//                .carrier()
//                .address()
//                .orderItems()
                .build();
        orderRepo.save(order);

        OrderRes orderRes = OrderRes.builder()
                .trackingNumber(order.getTrackingNumber())
                .paymentMethod(order.getPaymentMethod())
                //.status()
//                .paymentStatus()
//                .totalPrice()
//                .shippingFee()
//                .discountAmount()
//                .grandTotal()
//                .discount()
//                .estimatedDelivery()
//                .carrier()
//                .address()
//                .orderItems()
                .build();
        return orderRes;
    }

    public String generateTrackingNumber(String prefix) {
        String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        return prefix + uuid.substring(0, 8);
    }
}
