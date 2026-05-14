package com.nkh.ecommercebackend.mapper;

import com.nkh.ecommercebackend.dto.response.OrderItemRes;
import com.nkh.ecommercebackend.entity.OrderItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    List<OrderItemRes> toOrderItemResList(List<OrderItem> orderItemList);
}
