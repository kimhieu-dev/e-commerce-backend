package com.nkh.ecommercebackend.service.factory;

import com.nkh.ecommercebackend.common.OrderStatus;
import com.nkh.ecommercebackend.common.PaymentMethod;
import com.nkh.ecommercebackend.common.PaymentStatus;
import com.nkh.ecommercebackend.dto.request.GenerateOrderReq;
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
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OrderFactory {
    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final PaymentMethodStrategyFactory paymentMethodStrategyFactory;
    private final TrackingLogRepo trackingLogRepo;
    private final TrackingNumberGenerator trackingNumberGenerator;
    private final CarrierRepo carrierRepo;

    public Order generateOrder(GenerateOrderReq generateOrderReq) {

        PaymentMethodStrategy strategy = paymentMethodStrategyFactory.create(generateOrderReq.getPaymentMethod());
        PaymentStatus paymentStatus = strategy.apply(generateOrderReq.getPaymentMethod());

        // trong thuc te se tu dong gan cho don vi van chuyen gan nhat
        Carrier carrier = carrierRepo.findById("760e4dae-c885-41ba-88b9-ef930dd941a4")
                .orElseThrow(() -> new BusinessException(ErrorCode.CARRIER_NOT_FOUND));

        String trackingNumber = trackingNumberGenerator.generateTrackingNumber(carrier.getName());

        Order order = Order.builder()
                .trackingNumber(trackingNumber)
                .user(generateOrderReq.getUser())
                .paymentMethod(generateOrderReq.getPaymentMethod())
                .status(OrderStatus.PENDING)
                .paymentStatus(paymentStatus)
                .totalPrice(generateOrderReq.getSummary().getSubtotal())
                .shippingFee(generateOrderReq.getSummary().getShippingFee())
                .discountAmount(generateOrderReq.getSummary().getDiscountAmount())
                .grandTotal(generateOrderReq.getSummary().getTotalAmount())
                .discount(generateOrderReq.getDiscount())
                .estimatedDelivery(LocalDate.now().plusDays(carrier.getEstimatedDays()))
                .carrier(carrier)
                .carrierName(carrier.getName())
                .address(generateOrderReq.getAddress())
                .userAddress(String.join(", ",
                        generateOrderReq.getAddress().getProvince(),
                        generateOrderReq.getAddress().getDistrict(),
                        generateOrderReq.getAddress().getWard(),
                        generateOrderReq.getAddress().getDetailAddress()))
                .build();
        orderRepo.save(order);

        List<OrderItem> orderItems = new ArrayList<>();
        for (Product product : generateOrderReq.getProducts()) {
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(generateOrderReq.getProductQuantityMap().get(product.getId()))
                    .price(product.getBasePrice())
                    .build();
            orderItems.add(orderItem);
        }
        orderItemRepo.saveAll(orderItems);

        TrackingLog trackingLog = TrackingLog.builder()
                .order(order)
                .fromStatus(order.getStatus())
                .toStatus(order.getStatus())
                .note("Order is created")
                .location("system location")
                .build();
        trackingLogRepo.save(trackingLog);

        return order;
    }
}
