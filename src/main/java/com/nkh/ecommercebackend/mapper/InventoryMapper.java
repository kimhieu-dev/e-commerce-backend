package com.nkh.ecommercebackend.mapper;

import com.nkh.ecommercebackend.entity.Inventory;
import com.nkh.ecommercebackend.dto.response.InventoryRes;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    InventoryRes toInventoryRes(Inventory inventory);
}
