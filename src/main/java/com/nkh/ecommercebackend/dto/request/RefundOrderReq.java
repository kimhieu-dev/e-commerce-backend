package com.nkh.ecommercebackend.dto.request;

import com.nkh.ecommercebackend.common.UserOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefundOrderReq implements Serializable {
    private UserOrderStatus status;
    private String reason;
}
