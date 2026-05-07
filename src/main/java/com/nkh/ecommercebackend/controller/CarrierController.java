package com.nkh.ecommercebackend.controller;

import com.nkh.ecommercebackend.dto.response.BaseResponse;
import com.nkh.ecommercebackend.dto.response.CarrierRes;
import com.nkh.ecommercebackend.service.CarrierService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carriers")
@RequiredArgsConstructor
public class CarrierController {
    private final CarrierService carrierService;

    @GetMapping
    public BaseResponse<List<CarrierRes>>  getCarriers() {
        List<CarrierRes> response = carrierService.getCarriers();
        return BaseResponse.success(response);
    }
}
