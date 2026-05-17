package com.nkh.ecommercebackend.controller;

import com.nkh.ecommercebackend.dto.request.*;
import com.nkh.ecommercebackend.dto.response.*;
import com.nkh.ecommercebackend.service.OrderService;
import com.nkh.ecommercebackend.service.SummaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {
    private final OrderService orderService;
    private final SummaryService summaryService;

    @PostMapping
    public BaseResponse<OrderRes> createOrder(@RequestBody @Valid CreateOrderReq request) {
        OrderRes response = orderService.createOrder(request);
        return BaseResponse.success(response);
    }

//    @GetMapping("/summary")
//    public BaseResponse<SummaryRes> getSummary(@RequestBody String discountCode, @RequestBody String cartId) {
//        SummaryRes response = orderService.getSummary(discountCode);
//        return BaseResponse.success(response);
//    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public BaseResponse<List<OrderRes>> getOrders(OrderFilterReq request, @PageableDefault(size = 5, page = 0) Pageable pageable) {
        List<OrderRes> response = orderService.getOrders(request, pageable);
        return BaseResponse.success(response);
    }


    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/{id}/approve")
    public BaseResponse<OrderRes> approveOrder(@PathVariable String id, @RequestBody @Valid ApproveOrderReq request) {
        OrderRes response = orderService.approveOrder(id, request);
        return BaseResponse.success(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/{id}/reject")
    public BaseResponse<OrderRes> rejectOrder(@PathVariable String id, @RequestBody @Valid RejectOrderReq request) {
        OrderRes response = orderService.rejectOrder(id, request);
        return BaseResponse.success(response);
    }

    @PreAuthorize("hasAnyRole('SHIPPER')")
    @PatchMapping("/{id}/pickup")
    public BaseResponse<OrderRes> pickupOrder(@PathVariable String id, @RequestBody @Valid PickupOrderReq request) {
        OrderRes response = orderService.pickupOrder(id, request);
        return BaseResponse.success(response);
    }

    @PreAuthorize("hasAnyRole('SHIPPER')")
    @PatchMapping("/{id}/ship")
    public BaseResponse<OrderRes> shipOrder(@PathVariable String id, @RequestBody @Valid ShipOrderReq request){
        OrderRes response = orderService.shipOrder(id, request);
        return BaseResponse.success(response);
    }


    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/overview")
    public BaseResponse<OrderOverviewRes> getOverview(@RequestParam(value = "fromDate", required = false) LocalDate fromDate,
                                                      @RequestParam(value = "toDate", required = false) LocalDate toDate) {
        OrderOverviewRes response = orderService.getOverview(fromDate, toDate);
        return BaseResponse.success(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/today-statistics")
    public BaseResponse<TodayStatisticsRes> getTodayStatistics() {
        TodayStatisticsRes response = orderService.getTodayStatistics();
        return BaseResponse.success(response);
    }

    @GetMapping("/{id}/tracking-logs")
    public BaseResponse<List<TrackingLogRes>> getTrackingLogs(@PathVariable String id) {
        List<TrackingLogRes> response = orderService.getTrackingLogs(id);
        return BaseResponse.success(response);
    }

    @GetMapping("/{id}")
    public BaseResponse<OrderDetailRes> getOrderDetail(@PathVariable String id) {
        OrderDetailRes response = orderService.getOrderDetail(id);
        return BaseResponse.success(response);
    }

    @GetMapping("/my")
    public BaseResponse<List<MyOrdersRes>> getMyOrders(MyOrderFilterReq request,
                                                       @PageableDefault(size = 5, page = 0) Pageable pageable) {
        List<MyOrdersRes> response = orderService.getMyOrders(request, pageable);
        return BaseResponse.success(response);
    }
}
