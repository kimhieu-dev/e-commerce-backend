package com.nkh.ecommercebackend.dto.response;

import com.nkh.ecommercebackend.common.OrderStatus;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackingLogRes implements Serializable {
    private OrderStatus fromStatus;
    private OrderStatus toStatus;
    private String note;
    private String location;
    private LocalDateTime updatedAt;
}
