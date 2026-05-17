package com.nkh.ecommercebackend.dto.request;

import com.nkh.ecommercebackend.common.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliverOrderReq implements Serializable {
    private OrderStatus status = OrderStatus.DELIVERED;
}
