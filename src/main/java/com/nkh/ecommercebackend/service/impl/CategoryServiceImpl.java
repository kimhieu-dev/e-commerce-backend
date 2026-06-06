package com.nkh.ecommercebackend.service.impl;

import ch.qos.logback.classic.spi.IThrowableProxy;
import com.nkh.ecommercebackend.dto.request.CreateCategoryReq;
import com.nkh.ecommercebackend.dto.request.UpdateCategoryReq;
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

import java.util.List;

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

    @Override
    public CategoryRes update(UpdateCategoryReq request) {

        Category category = categoryRepo.findById(request.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        if (request.getParentId() != null) {
            Boolean isExisted = categoryRepo.existsByParentId(request.getParentId());
            if (!isExisted) {
                throw new BusinessException(ErrorCode.CATEGORY_NOT_FOUND);
            }
        }
        category.setParentId(request.getParentId());
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        categoryRepo.save(category);

        return CategoryRes.builder()
                .parentId(category.getParentId())
                .name(category.getName())
                .slug(category.getSlug())
                .description(category.getDescription())
                .build();
    }

    @Override
    public void delete(String id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
        category.setDeleted(true);
        categoryRepo.save(category);
    }

    @Override
    public List<CategoryRes> get() {
        List<Category> allCategories = categoryRepo.findAll();
        if (allCategories.isEmpty()) return List.of();
        
        List<CategoryRes> allDtos = categoryMapper.toCategoryRes(allCategories);
        if (allDtos == null || allDtos.isEmpty()) return List.of();

        List<CategoryRes> rootCategories = allDtos.stream()
                .filter(category -> category.getParentId() == null 
                        || category.getParentId().isEmpty() 
                        || "null".equalsIgnoreCase(category.getParentId().trim()))
                .peek(category -> category.setChildren(getChildren(category, allDtos)))
                .toList();
        
        // Fallback: Nếu logic phân cấp trả về rỗng nhưng có data, trả về list phẳng
        if (rootCategories.isEmpty() && !allDtos.isEmpty()) {
            return allDtos;
        }
        
        return rootCategories;
    }

    private List<CategoryRes> getChildren(CategoryRes parent, List<CategoryRes> allDtos) {
        if (parent.getId() == null) return List.of();
        return allDtos.stream()
                .filter(category -> parent.getId().equals(category.getParentId()))
                .peek(category -> category.setChildren(getChildren(category, allDtos)))
                .toList();
    }
}
