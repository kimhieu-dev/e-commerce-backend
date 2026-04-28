package com.nkh.ecommercebackend.mapper;

import com.nkh.ecommercebackend.dto.response.CartRes;
import com.nkh.ecommercebackend.entity.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartRes toCartRes(Cart cart);
}
