package com.nkh.ecommercebackend.mapper;

import com.nkh.ecommercebackend.dto.response.ProductDetailRes;
import com.nkh.ecommercebackend.dto.response.ProductRes;
import com.nkh.ecommercebackend.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "categoryId", source = "category.id")
    ProductRes toProductRes(Product product);

    List<ProductRes> toProductResList(List<Product> productList);

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "description", source = "productDetail.description")
    @Mapping(target = "weight", source = "productDetail.weight")
    @Mapping(target = "length", source = "productDetail.length")
    @Mapping(target = "width", source = "productDetail.width")
    @Mapping(target = "height", source = "productDetail.height")
    ProductDetailRes toProductDetailRes(Product product);
}
