package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.common.OrderStatus;
import com.nkh.ecommercebackend.common.PaymentMethod;
import com.nkh.ecommercebackend.common.PaymentStatus;
import com.nkh.ecommercebackend.dto.request.ApproveOrderReq;
import com.nkh.ecommercebackend.dto.request.CreateOrderReq;
import com.nkh.ecommercebackend.dto.request.OrderFilterReq;
import com.nkh.ecommercebackend.dto.request.RejectOrderReq;
import com.nkh.ecommercebackend.dto.response.OrderRes;
import com.nkh.ecommercebackend.dto.response.OverviewRes;
import com.nkh.ecommercebackend.dto.response.SummaryRes;
import com.nkh.ecommercebackend.dto.response.TodayStatisticsRes;
import com.nkh.ecommercebackend.entity.*;
import com.nkh.ecommercebackend.exception.BusinessException;
import com.nkh.ecommercebackend.exception.ErrorCode;
import com.nkh.ecommercebackend.mapper.OrderMapper;
import com.nkh.ecommercebackend.repository.*;
import com.nkh.ecommercebackend.service.OrderService;
import com.nkh.ecommercebackend.service.SummaryService;
import com.nkh.ecommercebackend.service.TrackingNumberGenerator;
import com.nkh.ecommercebackend.service.factory.OrderFactory;
import com.nkh.ecommercebackend.service.spec.OrderSpec;
import com.nkh.ecommercebackend.util.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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
    private final OrderRepo orderRepo;
    private final OrderMapper orderMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderRes createOrder(CreateOrderReq request) {

        User user = currentUserService.getUser();

        Cart cart = cartRepo.findByUsername(user.getUsername())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_DOES_NOT_HAVE_CART));

        Discount discount = discountRepo.findByCode(request.getDiscountCode())
                .orElseThrow(() -> new BusinessException(ErrorCode.DISCOUNT_NOT_FOUND));
        if (discount.getEndDate().isBefore(LocalDate.now())) {
            throw new BusinessException(ErrorCode.DISCOUNT_EXPIRED);
        }

        Carrier carrier = carrierRepo.findById(request.getCarrierId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CARRIER_NOT_FOUND));

        Address address = addressRepo.findById(request.getAddressId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ADDRESS_NOT_FOUND));

        SummaryRes summary = summaryService.getSummary(cart, discount);

        String trackingNumber = trackingNumberGenerator.generateTrackingNumber(carrier.getName());

        PaymentMethod paymentMethod = request.getPaymentMethod();

        return orderFactory.generateOrder(trackingNumber, user, cart, discount, carrier, address, paymentMethod, summary);
    }

    @Override
    public List<OrderRes> getOrders(OrderFilterReq request, Pageable pageable) {
        Specification<Order> specification = (root, query, criteriaBuilder)
                -> criteriaBuilder.conjunction();
        if (request.getTrackingNumber() != null && !request.getTrackingNumber().isEmpty()) {
            specification = specification.and(OrderSpec.likeTrackingNumber(request.getTrackingNumber()));
        }
        if (request.getOrderStatus() != null) {
            specification = specification.and(OrderSpec.equalOrderStatus(request.getOrderStatus()));
        }
        if (request.getMinPrice() != null) {
            specification = specification.and(OrderSpec.equalMinPrice(request.getMinPrice()));
        }
        if (request.getMaxPrice() != null) {
            specification = specification.and(OrderSpec.equalMaxPrice(request.getMaxPrice()));
        }
        if (request.getPaymentStatus() != null) {
            specification = specification.and(OrderSpec.equalPaymentStatus(request.getPaymentStatus()));
        }
        List<Order> orders = orderRepo.findAll(specification, pageable).getContent();
        return orderMapper.toOrderResList(orders);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderRes approveOrder(String id, ApproveOrderReq request) {
        request.setStatus(OrderStatus.CONFIRMED);
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        if (order.getStatus() == OrderStatus.CONFIRMED) {
            throw new BusinessException(ErrorCode.ORDER_ALREADY_CONFIRMED);
        }
        if (order.getPaymentStatus() == PaymentStatus.AWAITING_PAYMENT) {
            throw new BusinessException(ErrorCode.ORDER_AWAITING_PAYMENT);
        }
        order.setStatus(request.getStatus());
        orderRepo.save(order);
        int updatedUsedCount = discountRepo.increaseUsedCount(order.getDiscount().getId());
        if (updatedUsedCount == 0) {
            throw new BusinessException(ErrorCode.DISCOUNT_EXCEED);
        }
        int updatedReservedCount = discountRepo.decreaseReservedCount(order.getDiscount().getId());
        if (updatedReservedCount == 0) {
            throw new BusinessException(ErrorCode.DISCOUNT_EXCEED);
        }
        return orderMapper.toOrderRes(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderRes rejectOrder(String id, RejectOrderReq request) {
        request.setStatus(OrderStatus.REJECTED);
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        if (order.getStatus() == OrderStatus.REJECTED) {
            throw new BusinessException(ErrorCode.ORDER_ALREADY_REJECTED);
        }
        order.setStatus(request.getStatus());
        orderRepo.save(order);
        int updated = discountRepo.decreaseReservedCount(order.getDiscount().getId());
        if (updated == 0) {
            throw new BusinessException(ErrorCode.RESERVED_COUNT_NEGATIVE);
        }
        //TODO: nếu user trả tiền rồi thì hoàn tiền ?
        return orderMapper.toOrderRes(order);
    }

    @Override
    public OverviewRes getOverview(LocalDate fromDate, LocalDate toDate) {


        if (fromDate == null) fromDate = LocalDate.now().minusDays(30);
        if (toDate == null) toDate = LocalDate.now();

        LocalDateTime fromDateTime = fromDate.atStartOfDay();
        LocalDateTime toDateTime = toDate.atStartOfDay();

        //TODO dùng overview factory ?
        BigDecimal totalRevenue = orderRepo.calculateTotalRevenue(fromDateTime, toDateTime);
        Integer totalOrders = orderRepo.countTotalOrders(fromDateTime, toDateTime);
        Integer totalPending = orderRepo.countTotalPendingOrders(fromDateTime, toDateTime);
        Integer totalShipping = orderRepo.countTotalShippingOrders(fromDateTime, toDateTime);
        Integer totalFailed = orderRepo.countTotalFailedOrders(fromDateTime, toDateTime);
        return OverviewRes.builder()
                .totalRevenue(totalRevenue)
                .totalOrders(totalOrders)
                .totalPending(totalPending)
                .totalShipping(totalShipping)
                .totalFailed(totalFailed)
                .build();
    }

    @Override
    public TodayStatisticsRes getTodayStatistics() {
        LocalDateTime fromDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime toDateTime = LocalDate.now().plusDays(1).atStartOfDay();

        Integer totalOrdersToday = orderRepo.countTotalOrders(fromDateTime, toDateTime);
        Integer totalOrdersConfirmedToday = orderRepo.countTotalConfirmedOrders(fromDateTime, toDateTime);
        Integer totalOrdersPendingToday = orderRepo.countTotalPendingOrders(fromDateTime, toDateTime);

        return TodayStatisticsRes.builder()
                .totalOrdersToday(totalOrdersToday)
                .totalOrdersConfirmedToday(totalOrdersConfirmedToday)
                .totalOrdersPendingToday(totalOrdersPendingToday)
                .build();
    }

}
