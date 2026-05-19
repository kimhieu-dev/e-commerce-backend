package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.dto.request.CreateCarrierReq;
import com.nkh.ecommercebackend.dto.response.CarrierRes;

import java.util.List;

public interface CarrierService {
    List<CarrierRes> getCarriers();
    CarrierRes createCarrier(CreateCarrierReq request);
}
