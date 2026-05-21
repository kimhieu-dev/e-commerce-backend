package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.dto.request.CreateCategoryReq;
import com.nkh.ecommercebackend.dto.response.CategoryRes;
import com.nkh.ecommercebackend.entity.Category;
import com.nkh.ecommercebackend.exception.BusinessException;
import com.nkh.ecommercebackend.exception.ErrorCode;
import com.nkh.ecommercebackend.mapper.CategoryMapper;
import com.nkh.ecommercebackend.repository.CategoryRepo;
import com.nkh.ecommercebackend.service.CategoryService;
import com.nkh.ecommercebackend.service.SlugGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepo categoryRepo;
    private final SlugGenerator slugGenerator;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryRes create(CreateCategoryReq request) {
        if (request.getParentId() != null) {
            Boolean isExisted = categoryRepo.existsByParentId(request.getParentId());
            if (!isExisted) {
                throw new BusinessException(ErrorCode.CATEGORY_NOT_FOUND);
            }
        }
        String slug = slugGenerator.generateSlug(request.getName());

        if (!categoryRepo.existsBySlug(slug)) {
            throw new BusinessException(ErrorCode.SLUG_EXISTED);
        }

        Category category = categoryMapper.toCategory(request);
        category.setSlug(slug);

        categoryRepo.save(category);

        return CategoryRes.builder()
                .parentId(request.getParentId())
                .name(request.getName())
                .slug(slug)
                .description(request.getDescription())
                .build();
    }
}
