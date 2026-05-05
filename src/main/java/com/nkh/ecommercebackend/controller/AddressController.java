package com.nkh.ecommercebackend.controller;

import com.nkh.ecommercebackend.dto.response.AddressRes;
import com.nkh.ecommercebackend.dto.response.BaseResponse;
import com.nkh.ecommercebackend.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping()
    public BaseResponse<List<AddressRes>> getAddresses() {
        List<AddressRes> response = addressService.getAddresses();
        return BaseResponse.success(response);
    }


}
