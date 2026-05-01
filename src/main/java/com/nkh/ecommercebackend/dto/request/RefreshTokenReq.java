package com.nkh.ecommercebackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RefreshTokenReq implements Serializable {
    String refreshToken;
}
