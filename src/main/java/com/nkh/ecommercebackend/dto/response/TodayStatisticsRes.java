package com.nkh.ecommercebackend.dto.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodayStatisticsRes implements Serializable {
    private Integer totalOrdersToday;
    private Integer totalOrdersConfirmedToday;
    private Integer totalOrdersPendingToday;
}
