package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.common.OrderStatus;
import com.nkh.ecommercebackend.dto.request.*;
import com.nkh.ecommercebackend.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    OrderRes createOrder(CreateOrderReq request);

    List<OrderRes> getOrders(OrderFilterReq request, Pageable pageable);

    OrderRes approveOrder(String id,ApproveOrderReq request);

    OrderRes rejectOrder(String id, RejectOrderReq request );

    OrderRes pickupOrder(String id, PickupOrderReq request);

    OrderRes shipOrder(String id, ShipOrderReq request);

    OrderOverviewRes getOverview(LocalDate fromDate, LocalDate toDate);

    TodayStatisticsRes getTodayStatistics();

    List<TrackingLogRes> getTrackingLogs(String id);

    OrderDetailRes getOrderDetail(String id);

    List<MyOrdersRes> getMyOrders(MyOrderFilterReq request, Pageable pageable);
}
