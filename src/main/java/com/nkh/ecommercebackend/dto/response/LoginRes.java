package com.nkh.ecommercebackend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginRes {
    private String token;
    private Boolean authenticated;
}
