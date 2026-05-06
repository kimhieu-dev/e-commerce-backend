package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.common.DiscountType;
import com.nkh.ecommercebackend.common.OrderStatus;
import com.nkh.ecommercebackend.common.PaymentMethod;
import com.nkh.ecommercebackend.common.PaymentStatus;
import com.nkh.ecommercebackend.dto.request.CreateOrderReq;
import com.nkh.ecommercebackend.dto.response.OrderRes;
import com.nkh.ecommercebackend.dto.response.SummaryRes;
import com.nkh.ecommercebackend.entity.*;
import com.nkh.ecommercebackend.exception.BusinessException;
import com.nkh.ecommercebackend.exception.ErrorCode;
import com.nkh.ecommercebackend.mapper.OrderMapper;
import com.nkh.ecommercebackend.repository.*;
import com.nkh.ecommercebackend.service.OrderService;
import com.nkh.ecommercebackend.service.SummaryService;
import com.nkh.ecommercebackend.util.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    private final CartItemRepo cartItemRepo;
    private final SummaryService summaryService;
    private final CartRepo cartRepo;
    private final OrderItemRepo orderItemRepo;
    private final OrderMapper orderMapper;

    @Override
    public OrderRes createOrder(CreateOrderReq request) {

        User user = currentUserService.getUser();

        Cart cart = cartRepo.findByUsername(user.getUsername())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_DOES_NOT_HAVE_CART));

        Discount discount = discountRepo.findByCode(request.getDiscountCode())
                .orElseThrow(() -> new BusinessException(ErrorCode.DISCOUNT_NOT_FOUND));

        Carrier carrier = carrierRepo.findById(request.getCarrierId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CARRIER_NOT_FOUND));

        Address address = addressRepo.findById(request.getAddressId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ADDRESS_NOT_FOUND));

        PaymentStatus paymentStatus;
        if (request.getPaymentMethod() == PaymentMethod.COD) {
            paymentStatus = PaymentStatus.UNPAID;
        } else {
            paymentStatus = PaymentStatus.AWAITING_PAYMENT;
        }

        SummaryRes summary = summaryService.getSummary(discount.getCode());

        Order order = Order.builder()
                .trackingNumber(generateTrackingNumber(carrier.getName()))
                .user(user)
                .paymentMethod(request.getPaymentMethod())
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

        List<CartItem> cartItemList = cartItemRepo.findAllByCartIdAndCheckedTrue(cart.getId());

        List<OrderItem> orderItemList = new ArrayList<>();

        for (CartItem cartItem : cartItemList) {
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getProduct().getBasePrice())
                    .build();
            orderItemList.add(orderItem);
        }
        orderItemRepo.saveAll(orderItemList);

        return orderMapper.toOrderRes(order);
    }

    public String generateTrackingNumber(String prefix) {
        String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        return prefix + uuid.substring(0, 8);
    }
}
