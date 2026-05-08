package com.nkh.ecommercebackend.service.impl;

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
import com.nkh.ecommercebackend.service.TrackingNumberGenerator;
import com.nkh.ecommercebackend.service.factory.OrderFactory;
import com.nkh.ecommercebackend.util.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final CurrentUserService currentUserService;
    private final DiscountRepo discountRepo;
    private final CarrierRepo carrierRepo;
    private final AddressRepo addressRepo;
    private final SummaryService summaryService;
    private final CartRepo cartRepo;
    private final OrderFactory orderFactory;
    private final TrackingNumberGenerator trackingNumberGenerator;

    @Override
    @Transactional(rollbackFor = Exception.class)
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

        SummaryRes summary = summaryService.getSummary(discount.getCode());

        String trackingNumber = trackingNumberGenerator.generateTrackingNumber(carrier.getName());

        PaymentMethod paymentMethod = request.getPaymentMethod();

        return orderFactory.generateOrder(trackingNumber, user, cart, discount, carrier, address, paymentMethod, summary);
    }

}
