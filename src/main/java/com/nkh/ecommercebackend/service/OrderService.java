package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.dto.request.ApproveOrderReq;
import com.nkh.ecommercebackend.dto.request.CreateOrderReq;
import com.nkh.ecommercebackend.dto.request.OrderFilterReq;
import com.nkh.ecommercebackend.dto.request.RejectOrderReq;
import com.nkh.ecommercebackend.dto.response.*;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    OrderRes createOrder(CreateOrderReq request);

    List<OrderRes> getOrders(OrderFilterReq request, Pageable pageable);

    OrderRes approveOrder(String id,ApproveOrderReq request);

    OrderRes rejectOrder(String id, RejectOrderReq request );

    OrderOverviewRes getOverview(LocalDate fromDate, LocalDate toDate);

    TodayStatisticsRes getTodayStatistics();

    List<TrackingLogRes> getTrackingLogs(String id);

    OrderDetailRes getOrderDetail(String id);
}
