package com.nkh.ecommercebackend.dto.request;

import com.nkh.ecommercebackend.common.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RejectOrderReq implements Serializable {
    @NotNull
    private OrderStatus status = OrderStatus.REJECTED;
}
