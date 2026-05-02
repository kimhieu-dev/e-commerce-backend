package com.nkh.ecommercebackend.mapper;

import com.nkh.ecommercebackend.dto.response.ProductRes;
import com.nkh.ecommercebackend.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductRes toProductRes(Product product);

    List<ProductRes> toProductResList(List<Product> productList);
}
