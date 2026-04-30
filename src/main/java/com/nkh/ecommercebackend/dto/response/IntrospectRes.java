package com.nkh.ecommercebackend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class IntrospectRes implements Serializable {
    private Boolean valid;
}
