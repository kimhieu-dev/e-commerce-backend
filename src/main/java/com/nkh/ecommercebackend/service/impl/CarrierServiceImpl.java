package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.dto.response.CarrierRes;
import com.nkh.ecommercebackend.mapper.CarrierMapper;
import com.nkh.ecommercebackend.repository.CarrierRepo;
import com.nkh.ecommercebackend.service.CarrierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarrierServiceImpl implements CarrierService {
    private final CarrierRepo carrierRepo;
    private final CarrierMapper carrierMapper;

    @Override
    public List<CarrierRes> getCarriers() {
        return carrierMapper.toCarrierResList(carrierRepo.findAll());
    }
}
