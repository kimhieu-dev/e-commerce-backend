package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.dto.request.CreateCategoryReq;
import com.nkh.ecommercebackend.dto.request.UpdateCategoryReq;
import com.nkh.ecommercebackend.dto.response.CategoryRes;

public interface CategoryService {
    CategoryRes create(CreateCategoryReq request);
    CategoryRes update(UpdateCategoryReq request);
    void delete(String id);
}
