package com.nkh.ecommercebackend.controller;

import com.nkh.ecommercebackend.dto.request.ApproveOrderReq;
import com.nkh.ecommercebackend.dto.request.CreateOrderReq;
import com.nkh.ecommercebackend.dto.request.OrderFilterReq;
import com.nkh.ecommercebackend.dto.request.RejectOrderReq;
import com.nkh.ecommercebackend.dto.response.BaseResponse;
import com.nkh.ecommercebackend.dto.response.OrderRes;
import com.nkh.ecommercebackend.dto.response.OverviewRes;
import com.nkh.ecommercebackend.dto.response.SummaryRes;
import com.nkh.ecommercebackend.entity.Order;
import com.nkh.ecommercebackend.service.OrderService;
import com.nkh.ecommercebackend.service.SummaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.Pageable;
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

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/overview")
    public BaseResponse<OverviewRes> getOverview(@RequestParam(value = "from", required = false) LocalDate from,
                                                 @RequestParam(value = "to", required = false) LocalDate to) {
        OverviewRes response = orderService.getOverview(from,to);
        return BaseResponse.success(response);
    }
}
