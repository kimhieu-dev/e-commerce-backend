package com.nkh.ecommercebackend.dto.response;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OverviewRes implements Serializable {
    private BigDecimal totalRevenue;
    private Integer totalOrders;
    private Integer totalPending;
    private Integer totalShipping;
    private Integer totalFailed;
}
