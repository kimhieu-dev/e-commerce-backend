package com.nkh.ecommercebackend.mapper;

import com.nkh.ecommercebackend.dto.response.DiscountRes;
import com.nkh.ecommercebackend.entity.Discount;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DiscountMapper {
    List<DiscountRes> toDiscountResList(List<Discount> discount);
}
