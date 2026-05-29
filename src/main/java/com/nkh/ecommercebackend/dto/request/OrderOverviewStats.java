package com.nkh.ecommercebackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class OrderOverviewStats {
    private BigDecimal totalRevenue;
    private Integer totalOrders;
    private Integer totalPending;
    private Integer totalShipping;
    private Integer totalFailed;
}
