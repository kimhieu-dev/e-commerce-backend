package com.nkh.ecommercebackend.dto.request;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodayStatistics implements Serializable {
    private Integer totalOrdersToday;
    private Integer totalOrdersConfirmedToday;
    private Integer totalOrdersPendingToday;
}
