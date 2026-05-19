package com.nkh.ecommercebackend.controller;

import com.nkh.ecommercebackend.dto.request.CreateCarrierReq;
import com.nkh.ecommercebackend.dto.response.BaseResponse;
import com.nkh.ecommercebackend.dto.response.CarrierRes;
import com.nkh.ecommercebackend.service.CarrierService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public BaseResponse<CarrierRes> createCarrier(@RequestBody CreateCarrierReq request) {
        CarrierRes response = carrierService.createCarrier(request);
        return BaseResponse.success(response);
    }
}
