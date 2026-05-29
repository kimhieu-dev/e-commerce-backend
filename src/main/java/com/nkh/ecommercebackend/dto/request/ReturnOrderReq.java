package com.nkh.ecommercebackend.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ReturnOrderReq implements Serializable {
    private String note;
}
