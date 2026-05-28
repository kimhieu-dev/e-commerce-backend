package com.nkh.ecommercebackend.service.factory;

import com.nkh.ecommercebackend.common.OrderStatus;
import com.nkh.ecommercebackend.common.PaymentMethod;
import com.nkh.ecommercebackend.common.PaymentStatus;
import com.nkh.ecommercebackend.dto.response.OrderRes;
import com.nkh.ecommercebackend.dto.response.OrderSummary;
import com.nkh.ecommercebackend.entity.*;
import com.nkh.ecommercebackend.exception.BusinessException;
import com.nkh.ecommercebackend.exception.ErrorCode;
import com.nkh.ecommercebackend.mapper.OrderMapper;
import com.nkh.ecommercebackend.repository.*;
import com.nkh.ecommercebackend.service.PaymentMethodStrategy;
import com.nkh.ecommercebackend.service.TrackingNumberGenerator;
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
    private final PaymentMethodStrategyFactory paymentMethodStrategyFactory;
    private final TrackingLogRepo trackingLogRepo;
    private final TrackingNumberGenerator trackingNumberGenerator;
    private final CarrierRepo carrierRepo;

    public OrderRes generateOrder(User user, Discount discount, Address address, PaymentMethod paymentMethod, OrderSummary summary) {

        PaymentMethodStrategy strategy = paymentMethodStrategyFactory.create(paymentMethod);
        PaymentStatus paymentStatus = strategy.apply(paymentMethod);

        // trong thuc te se tu dong gan cho don vi van chuyen gan nhat
        Carrier carrier = carrierRepo.findById("760e4dae-c885-41ba-88b9-ef930dd941a4")
                .orElseThrow(() -> new BusinessException(ErrorCode.CARRIER_NOT_FOUND));

        String trackingNumber = trackingNumberGenerator.generateTrackingNumber(carrier.getName());

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
                .carrierName(carrier.getName())
                .address(address)
                .userAddress(String.join(", ",
                        address.getProvince(),
                        address.getDistrict(),
                        address.getWard(),
                        address.getDetailAddress()))
                .build();
        orderRepo.save(order);

//        orderItemRepo.saveAll(orderItemList);
//        order.setOrderItems(orderItemList);

        TrackingLog trackingLog = TrackingLog.builder()
                .order(order)
                .fromStatus(order.getStatus())
                .toStatus(order.getStatus())
                .note("init note")
                .location("init location")
                .build();
        trackingLogRepo.save(trackingLog);

        return orderMapper.toOrderRes(order);
    }
}
