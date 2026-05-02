package com.nkh.ecommercebackend.mapper;

import com.nkh.ecommercebackend.dto.request.AddItemToCartReq;
import com.nkh.ecommercebackend.dto.response.CartItemRes;
import com.nkh.ecommercebackend.entity.CartItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    CartItem toCartItem(AddItemToCartReq addItemToCartReq);

    List<CartItemRes> toCartItemResList(List<CartItem> cartItemList);

    CartItemRes toCartItemRes(CartItem cartItem);
}
