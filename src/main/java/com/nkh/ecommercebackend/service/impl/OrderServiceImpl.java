package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.common.OrderStatus;
import com.nkh.ecommercebackend.common.PaymentMethod;
import com.nkh.ecommercebackend.common.PaymentStatus;
import com.nkh.ecommercebackend.common.UserOrderStatus;
import com.nkh.ecommercebackend.dto.request.*;
import com.nkh.ecommercebackend.dto.response.*;
import com.nkh.ecommercebackend.entity.*;
import com.nkh.ecommercebackend.exception.BusinessException;
import com.nkh.ecommercebackend.exception.ErrorCode;
import com.nkh.ecommercebackend.mapper.AddressMapper;
import com.nkh.ecommercebackend.mapper.OrderItemMapper;
import com.nkh.ecommercebackend.mapper.OrderMapper;
import com.nkh.ecommercebackend.mapper.TrackingLogMapper;
import com.nkh.ecommercebackend.repository.*;
import com.nkh.ecommercebackend.service.OrderService;
import com.nkh.ecommercebackend.service.SummaryService;
import com.nkh.ecommercebackend.service.TrackingNumberGenerator;
import com.nkh.ecommercebackend.service.factory.OrderFactory;
import com.nkh.ecommercebackend.service.spec.OrderSpec;
import com.nkh.ecommercebackend.util.CurrentUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Slf4j
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
    private final TrackingLogRepo trackingLogRepo;
    private final TrackingLogMapper trackingLogMapper;
    private final AddressMapper addressMapper;
    private final OrderItemRepo orderItemRepo;
    private final OrderItemMapper orderItemMapper;

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

        OrderStatus orderStatus = order.getStatus();

        order.setStatus(request.getStatus());
        orderRepo.save(order);

        TrackingLog trackingLog = TrackingLog.builder()
                .order(order)
                .fromStatus(orderStatus)
                .toStatus(order.getStatus())
                .note("order confirmed")
                .location("init location")
                .build();
        trackingLogRepo.save(trackingLog);

        int updatedUsedCount = discountRepo.increaseUsedCount(order.getDiscount().getId());
        if (updatedUsedCount == 0) {
            throw new BusinessException(ErrorCode.DISCOUNT_EXCEED);
        }
        int updatedReservedCount = discountRepo.decreaseReservedCount(order.getDiscount().getId());
        if (updatedReservedCount == 0) {
            throw new BusinessException(ErrorCode.DISCOUNT_EXCEED);
        }
        //TODO: thêm tracking log
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
        OrderStatus orderStatus = order.getStatus();

        order.setStatus(request.getStatus());
        orderRepo.save(order);

        TrackingLog trackingLog = TrackingLog.builder()
                .order(order)
                .fromStatus(orderStatus)
                .toStatus(order.getStatus())
                .note("order confirmed")
                .location("init location")
                .build();
        trackingLogRepo.save(trackingLog);

        int updated = discountRepo.decreaseReservedCount(order.getDiscount().getId());
        if (updated == 0) {
            throw new BusinessException(ErrorCode.RESERVED_COUNT_NEGATIVE);
        }
        //TODO: nếu user trả tiền rồi thì hoàn tiền ?
        //TODO: thêm tracking log
        return orderMapper.toOrderRes(order);
    }

    @Override
    public OrderRes pickupOrder(String id, PickupOrderReq request) {
        request.setStatus(OrderStatus.PICKING);
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        if (order.getStatus() == OrderStatus.PICKING) {
            throw new BusinessException(ErrorCode.ORDER_ALREADY_PICKING);
        }
        OrderStatus orderStatus = order.getStatus();

        order.setStatus(request.getStatus());
        orderRepo.save(order);

        TrackingLog trackingLog = TrackingLog.builder()
                .order(order)
                .fromStatus(orderStatus)
                .toStatus(order.getStatus())
                .note("order picked up")
                .location("init location")
                .build();
        trackingLogRepo.save(trackingLog);

        return orderMapper.toOrderRes(order);
    }

    @Override
    public OrderRes shipOrder(String id, ShipOrderReq request) {
        request.setStatus(OrderStatus.SHIPPING);
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        if (order.getStatus() == OrderStatus.SHIPPING) {
            throw new BusinessException(ErrorCode.ORDER_ALREADY_SHIPPING);
        }
        OrderStatus orderStatus = order.getStatus();

        order.setStatus(request.getStatus());
        orderRepo.save(order);

        TrackingLog trackingLog = TrackingLog.builder()
                .order(order)
                .fromStatus(orderStatus)
                .toStatus(order.getStatus())
                .note("order shipping")
                .location("shipping location")
                .build();
        trackingLogRepo.save(trackingLog);

        return orderMapper.toOrderRes(order);
    }

    @Override
    public OrderRes deliverOrder(String id, DeliverOrderReq request) {
        request.setStatus(OrderStatus.DELIVERED);
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new BusinessException(ErrorCode.ORDER_ALREADY_DELIVERED);
        }
        OrderStatus orderStatus = order.getStatus();

        order.setStatus(request.getStatus());
        orderRepo.save(order);

        TrackingLog trackingLog = TrackingLog.builder()
                .order(order)
                .fromStatus(orderStatus)
                .toStatus(order.getStatus())
                .note("order delivered")
                .location("user address location")
                .build();
        trackingLogRepo.save(trackingLog);

        return orderMapper.toOrderRes(order);
    }

    @Override
    public OrderRes refundOrder(String id, RefundOrderReq request) {
        request.setStatus(UserOrderStatus.RETURNED);
        User user = currentUserService.getUser();
        Optional<Order> order = orderRepo.findById(id);
        if (order.isEmpty()) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        if (!order.get().getUser().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.USER_DOES_NOT_HAVE_PRIVILEGE);
        }
        order.get().setStatus(OrderStatus.RETURNING);
        orderRepo.save(order.get());
        return orderMapper.toOrderRes(order.get());
    }

    @Override
    public OrderOverviewRes getOverview(LocalDate fromDate, LocalDate toDate) {

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
        return OrderOverviewRes.builder()
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

    @Override
    public List<TrackingLogRes> getTrackingLogs(String id) {
        User user = currentUserService.getUser();

        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        if (!order.getUser().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.YOU_DO_NOT_HAVE_PRIVILEGE);
        }

        List<TrackingLog> trackingLogs = trackingLogRepo.findAllByOrderId(id);
        if (trackingLogs == null || trackingLogs.isEmpty()) {
            throw new BusinessException(ErrorCode.TRACKING_LOGS_NOT_FOUND);
        }
        return trackingLogMapper.toTrackingLogResList(trackingLogs);
    }

    @Override
    public OrderDetailRes getOrderDetail(String id) {
        User user = currentUserService.getUser();
        Order order = orderRepo.findByIdTrackingLog(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        if (!order.getUser().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.YOU_DO_NOT_HAVE_PRIVILEGE);
        }
        List<OrderItem> orderItemList = order.getOrderItems();
        if (orderItemList == null || orderItemList.isEmpty()) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        List<OrderItemRes> orderItemResList = orderItemMapper.toOrderItemResList(orderItemList);
        return OrderDetailRes.builder()
                .trackingNumber(order.getTrackingNumber())
                .paymentMethod(order.getPaymentMethod())
                .status(order.getStatus())
                .grandTotal(order.getGrandTotal())
                .estimatedDelivery(order.getEstimatedDelivery())
                .address(addressMapper.toAddressRes(order.getAddress()))
                .orderItems(orderItemResList)
                .build();
    }

    @Override
    public List<MyOrdersRes> getMyOrders(MyOrderFilterReq request, Pageable pageable) {
        User user = currentUserService.getUser();
        List<Order> orders;
        switch (request.getStatus()) {
            case PENDING ->
                    orders = orderRepo.findAllByUserIdAndStatusAndDeletedFalse(user.getId(), OrderStatus.PENDING, pageable);
            case PICKING ->
                    orders = orderRepo.findAllByUserIdAndStatusAndDeletedFalse(user.getId(), OrderStatus.PICKING, pageable);
            case SHIPPING ->
                    orders = orderRepo.findAllByUserIdAndStatusAndDeletedFalse(user.getId(), OrderStatus.SHIPPING, pageable);
            case DELIVERED ->
                    orders = orderRepo.findAllByUserIdAndStatusAndDeletedFalse(user.getId(), OrderStatus.DELIVERED, pageable);
            case FAILED ->
                    orders = orderRepo.findAllByUserIdAndStatusAndDeletedFalse(user.getId(), OrderStatus.FAILED, pageable);
            case RETURNED ->
                    orders = orderRepo.findAllByUserIdAndStatusAndDeletedFalse(user.getId(), OrderStatus.RETURNING, pageable);
            default -> orders = orderRepo.findAllByUserIdAndDeletedFalse(user.getId());
        }
        return orderMapper.toMyOrders(orders);
    }

    @Override
    public void sendMail() {
        LocalDate today = LocalDate.now();

        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        Pageable pageable = PageRequest.of(0, 100);
        List<Order> orders = orderRepo.findOrdersForSendMail(OrderStatus.DELIVERED, startOfDay, endOfDay, pageable);

        for (Order order : orders) {
            try {
                System.out.println("Sending mail...");
//                NotificationService.sendMail(order);
            } catch (Exception e) {
                log.error("Error:{}", e.getMessage());
            }
        }
    }

}
