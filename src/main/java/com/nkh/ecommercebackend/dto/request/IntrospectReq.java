package com.nkh.ecommercebackend.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class IntrospectReq implements Serializable {
    private String token;
}
