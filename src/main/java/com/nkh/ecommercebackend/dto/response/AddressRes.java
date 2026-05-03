package com.nkh.ecommercebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressRes implements Serializable {
    private String recipientName;
    private String phoneNumber;
    private String province;
    private String district;
    private String ward;
    private String detailAddress;
}
