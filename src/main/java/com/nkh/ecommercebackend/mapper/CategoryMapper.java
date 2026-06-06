package com.nkh.ecommercebackend.mapper;

import com.nkh.ecommercebackend.dto.request.CreateCategoryReq;
import com.nkh.ecommercebackend.dto.request.UpdateCategoryReq;
import com.nkh.ecommercebackend.dto.response.CategoryRes;
import com.nkh.ecommercebackend.entity.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CreateCategoryReq request);
    Category toCategory(UpdateCategoryReq request);

    CategoryRes toCategoryRes(Category category);
    List<CategoryRes> toCategoryRes(List<Category> categories);
}
