package com.nkh.ecommercebackend.mapper;

import com.nkh.ecommercebackend.dto.request.CreateCarrierReq;
import com.nkh.ecommercebackend.dto.response.CarrierRes;
import com.nkh.ecommercebackend.entity.Carrier;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarrierMapper {
    List<CarrierRes> toCarrierResList(List<Carrier> carriers);
    Carrier toCarrier(CreateCarrierReq request);
    CarrierRes toCarrierRes(Carrier carrier);
}
