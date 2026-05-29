package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.common.OrderStatus;
import com.nkh.ecommercebackend.common.PaymentMethod;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final CurrentUserService currentUserService;
    private final DiscountRepo discountRepo;
    private final AddressRepo addressRepo;
    private final SummaryService summaryService;
    private final OrderFactory orderFactory;
    private final OrderRepo orderRepo;
    private final OrderMapper orderMapper;
    private final TrackingLogRepo trackingLogRepo;
    private final TrackingLogMapper trackingLogMapper;

    private final OrderItemMapper orderItemMapper;
    private final ProductRepo productRepo;
    private final InventoryRepo inventoryRepo;
    private final CartItemRepo cartItemRepo;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderRes createOrder(CreateOrderReq request) {

        //1. lay ra tt user
        //2. validate tt product (check ton tai), discount code (check ton tai),..., so sanh address trong request va cua user
        //3. tao order save xuong db (generacte order, generate sku, validate ton kho,...)
        //4. tao order items save xuong db
        //5. xoa cart items (neu co)

        User user = currentUserService.getUser();

        Map<String, Integer> productQuantityMap = request.getOrderItems().stream()
                .collect(Collectors.toMap(OrderItemReq::getProductId, OrderItemReq::getQuantity));

        List<Product> products = productRepo.findAllByIds(productQuantityMap.keySet());
        if (products.size() != productQuantityMap.size()) {
            throw new BusinessException(ErrorCode.SOME_PRODUCT_NOT_EXIST);
        }
        Discount discount = discountRepo.findByCode(request.getDiscountCode())
                .orElseThrow(() -> new BusinessException(ErrorCode.DISCOUNT_NOT_FOUND));
        if (discount.getEndDate().isBefore(LocalDate.now())) {
            throw new BusinessException(ErrorCode.DISCOUNT_EXPIRED);
        }
        if (discount.getReservedCount() + discount.getUsedCount() > discount.getUsageLimit()) {
            throw new BusinessException(ErrorCode.DISCOUNT_EXCEED);
        }
        discount.setReservedCount(discount.getReservedCount() + 1);
        discountRepo.save(discount);

        Address address = addressRepo.findById(request.getAddressId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ADDRESS_NOT_FOUND));

        //check ton kho va tang reserved count
        List<Inventory> inventories = new ArrayList<>();
        for (Product product : products) {
            Inventory inventory = product.getInventory();
            if (inventory.getQuantityInStock() - inventory.getReservedQuantity()
                    >= productQuantityMap.get(product.getId())) {
                inventory.setReservedQuantity(inventory.getReservedQuantity() + productQuantityMap.get(product.getId()));
                inventories.add(inventory);
            } else {
                throw new BusinessException(ErrorCode.PRODUCT_OUT_OF_STOCK);
            }
        }
        inventoryRepo.saveAll(inventories);

        PaymentMethod paymentMethod = request.getPaymentMethod();

        OrderSummary summary = summaryService.getSummary(productQuantityMap, discount.getCode());
        Order order = orderFactory.generateOrder(
                new GenerateOrderReq(user,
                        products,
                        productQuantityMap,
                        discount,
                        address,
                        paymentMethod,
                        summary)
        );

        if (user.getCart() != null) {
            Set<String> purchasedProductIds = productQuantityMap.keySet();
            cartItemRepo.softDeletePurchasedItems(user.getCart().getId(), purchasedProductIds);
        }

        return orderMapper.toOrderRes(order);
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
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        OrderStatus oldStatus = order.getStatus();
        order.setStatus(OrderStatus.CONFIRMED);
        orderRepo.save(order);

        Discount discount = discountRepo.findById(order.getDiscount().getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.DISCOUNT_NOT_FOUND));
        discount.setReservedCount(discount.getReservedCount() - 1);
        discount.setUsedCount(discount.getUsedCount() + 1);
        //da su dung version optimistic lock
        discountRepo.save(discount);

        TrackingLog trackingLog = TrackingLog.builder()
                .order(order)
                .fromStatus(oldStatus)
                .toStatus(order.getStatus())
                .note(request.getNote())
                .location("admin confirmed")
                .build();
        trackingLogRepo.save(trackingLog);

        return orderMapper.toOrderRes(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderRes rejectOrder(String id, RejectOrderReq request) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        OrderStatus oldStatus = order.getStatus();
        order.setStatus(OrderStatus.REJECTED);
        orderRepo.save(order);

        Discount discount = discountRepo.findById(order.getDiscount().getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.DISCOUNT_NOT_FOUND));
        discount.setReservedCount(discount.getReservedCount() - 1);
        //da su dung version optimistic lock
        discountRepo.save(discount);

        TrackingLog trackingLog = TrackingLog.builder()
                .order(order)
                .fromStatus(oldStatus)
                .toStatus(order.getStatus())
                .note(request.getNote())
                .location("admin rejected")
                .build();
        trackingLogRepo.save(trackingLog);

        return orderMapper.toOrderRes(order);
    }

    @Override
    public OrderRes pickupOrder(String id, PickupOrderReq request) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        OrderStatus oldStatus = order.getStatus();
        order.setStatus(OrderStatus.PICKING);
        orderRepo.save(order);

        TrackingLog trackingLog = TrackingLog.builder()
                .order(order)
                .fromStatus(oldStatus)
                .toStatus(order.getStatus())
                .note("order picking up")
                .location("shipper pinking up order")
                .build();
        trackingLogRepo.save(trackingLog);

        return orderMapper.toOrderRes(order);
    }

    @Override
    public OrderRes shipOrder(String id, ShipOrderReq request) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        OrderStatus oldStatus = order.getStatus();
        order.setStatus(OrderStatus.SHIPPING);
        orderRepo.save(order);

        TrackingLog trackingLog = TrackingLog.builder()
                .order(order)
                .fromStatus(oldStatus)
                .toStatus(order.getStatus())
                .note(request.getNote())
                .location("shipping location")
                .build();
        trackingLogRepo.save(trackingLog);

        return orderMapper.toOrderRes(order);
    }

    @Override
    public OrderRes deliverOrder(String id, DeliverOrderReq request) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        OrderStatus oldStatus = order.getStatus();
        order.setStatus(OrderStatus.SHIPPING);
        orderRepo.save(order);

        TrackingLog trackingLog = TrackingLog.builder()
                .order(order)
                .fromStatus(oldStatus)
                .toStatus(order.getStatus())
                .note(request.getNote())
                .location(order.getUserAddress())
                .build();
        trackingLogRepo.save(trackingLog);

        return orderMapper.toOrderRes(order);
    }

    @Override
    public OrderRes failOrder(String id, FailOrderReq request) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        OrderStatus oldStatus = order.getStatus();
        order.setStatus(OrderStatus.FAILED);
        orderRepo.save(order);

        TrackingLog trackingLog = TrackingLog.builder()
                .order(order)
                .fromStatus(oldStatus)
                .toStatus(order.getStatus())
                .note(request.getNote())
                .location(order.getUserAddress())
                .build();
        trackingLogRepo.save(trackingLog);

        return orderMapper.toOrderRes(order);
    }

    @Override
    public OrderRes refundOrder(String id, RefundOrderReq request) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        OrderStatus oldStatus = order.getStatus();
        order.setStatus(OrderStatus.REFUND);
        orderRepo.save(order);

        TrackingLog trackingLog = TrackingLog.builder()
                .order(order)
                .fromStatus(oldStatus)
                .toStatus(order.getStatus())
                .note(request.getNote())
                .location(order.getUserAddress())
                .build();
        trackingLogRepo.save(trackingLog);

        return orderMapper.toOrderRes(order);
    }

    @Override
    public OrderRes returnOrder(String id, ReturnOrderReq request) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        OrderStatus oldStatus = order.getStatus();
        order.setStatus(OrderStatus.RETURNING);
        orderRepo.save(order);

        TrackingLog trackingLog = TrackingLog.builder()
                .order(order)
                .fromStatus(oldStatus)
                .toStatus(order.getStatus())
                .note(request.getNote())
                .location("Returning location")
                .build();
        trackingLogRepo.save(trackingLog);

        return orderMapper.toOrderRes(order);
    }

    @Override
    public OrderOverviewStats getOverview(LocalDate fromDate, LocalDate toDate) {

        if (fromDate == null) fromDate = LocalDate.now().minusDays(30);
        if (toDate == null) toDate = LocalDate.now();

        LocalDateTime fromDateTime = fromDate.atStartOfDay();
        LocalDateTime toDateTime = toDate.atStartOfDay();

        return orderRepo.getOverviewStats(fromDateTime, toDateTime);
    }

    @Override
    public TodayStatisticsRes getTodayStatistics() {
        LocalDateTime fromDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime toDateTime = LocalDate.now().plusDays(1).atStartOfDay();

        TodayStatistics todayStats = orderRepo.getTodayStats(fromDateTime, toDateTime);

        return TodayStatisticsRes.builder()
                .totalOrdersToday(todayStats.getTotalOrdersToday())
                .totalOrdersConfirmedToday(todayStats.getTotalOrdersConfirmedToday())
                .totalOrdersPendingToday(todayStats.getTotalOrdersPendingToday())
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
        List<OrderItem> orderItems = order.getOrderItems();
        List<OrderItemRes> orderItemResList = orderItemMapper.toOrderItemResList(orderItems);

        return OrderDetailRes.builder()
                .trackingNumber(order.getTrackingNumber())
                .paymentMethod(order.getPaymentMethod())
                .status(order.getStatus())
                .grandTotal(order.getGrandTotal())
                .estimatedDelivery(order.getEstimatedDelivery())
                .address(order.getUserAddress())
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
