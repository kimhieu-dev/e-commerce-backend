package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.dto.response.AddressRes;

import java.util.List;

public interface AddressService {
    List<AddressRes> getAddresses();
}
