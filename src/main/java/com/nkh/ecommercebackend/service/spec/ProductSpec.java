package com.nkh.ecommercebackend.service.spec;

import com.nkh.ecommercebackend.entity.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpec {
    public static Specification<Product> likeName(String name) {
        return new Specification<Product>() {
            @Override
            public @Nullable Predicate toPredicate(@NonNull Root<Product> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
                if (name == null || name.isEmpty()) {
                    return criteriaBuilder.conjunction();
                }
                return criteriaBuilder.like(root.get("name"), "%" + name + "%");
            }
        };
    }
    public static Specification<Product> likeSku(String sku) {
        return new Specification<Product>() {
            @Override
            public @Nullable Predicate toPredicate(@NonNull Root<Product> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
                if (sku == null || sku.isEmpty()) {
                    return criteriaBuilder.conjunction();
                }
                return criteriaBuilder.like(root.get("sku"), "%" + sku + "%");
            }
        };
    }
}
