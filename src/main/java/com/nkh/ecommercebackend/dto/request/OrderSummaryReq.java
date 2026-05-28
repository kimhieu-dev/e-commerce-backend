package com.nkh.ecommercebackend.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
public class OrderSummaryReq implements Serializable {
    private Map<String,Integer> productQuantityMap;
    private String discountCode;
}
