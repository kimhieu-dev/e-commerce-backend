package com.nkh.ecommercebackend.mapper;

import com.nkh.ecommercebackend.dto.response.OrderRes;
import com.nkh.ecommercebackend.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderRes toOrderRes(Order order);
}
