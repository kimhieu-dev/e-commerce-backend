package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.dto.request.CreateCarrierReq;
import com.nkh.ecommercebackend.dto.response.CarrierRes;
import com.nkh.ecommercebackend.entity.Carrier;
import com.nkh.ecommercebackend.exception.BusinessException;
import com.nkh.ecommercebackend.exception.ErrorCode;
import com.nkh.ecommercebackend.mapper.CarrierMapper;
import com.nkh.ecommercebackend.repository.CarrierRepo;
import com.nkh.ecommercebackend.service.CarrierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarrierServiceImpl implements CarrierService {
    private final CarrierRepo carrierRepo;
    private final CarrierMapper carrierMapper;

    @Override
    public List<CarrierRes> getCarriers() {
        return carrierMapper.toCarrierResList(carrierRepo.findAll());
    }

    @Override
    public CarrierRes createCarrier(CreateCarrierReq request) {
        Optional<Carrier> carrierOptional = carrierRepo.findByName(request.getName());
        if (carrierOptional.isPresent()){
            throw new BusinessException(ErrorCode.CARRIER_EXISTS);
        }
        Carrier carrier = carrierMapper.toCarrier(request);
        carrierRepo.save(carrier);

        return carrierMapper.toCarrierRes(carrier);
    }
}
