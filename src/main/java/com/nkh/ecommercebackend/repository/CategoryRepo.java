package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category,String> {
    List<Category> findByParentId(String parentId);

    Boolean existsByParentId(String parentId);

    boolean existsBySlug(String slug);
}
