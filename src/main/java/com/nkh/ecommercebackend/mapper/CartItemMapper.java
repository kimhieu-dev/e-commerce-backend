package com.nkh.ecommercebackend.mapper;

import com.nkh.ecommercebackend.dto.request.AddItemToCartReq;
import com.nkh.ecommercebackend.entity.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    CartItem toCartItem(AddItemToCartReq addItemToCartReq);
}
