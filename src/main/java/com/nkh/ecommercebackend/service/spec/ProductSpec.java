package com.nkh.ecommercebackend.service.spec;

import com.nkh.ecommercebackend.entity.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

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

    public static Specification<Product> greatThanOrEqualTo(BigDecimal minPrice) {
        return new Specification<Product>() {
            @Override
            public @Nullable Predicate toPredicate(@NonNull Root<Product> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
                if (minPrice == null) {
                    return criteriaBuilder.conjunction();
                }
                return criteriaBuilder.greaterThanOrEqualTo(root.get("basePrice"), minPrice);
            }
        };
    }

        public static Specification<Product> lessThanOrEqualTo(BigDecimal maxPrice) {
        return new Specification<Product>() {
            @Override
            public @Nullable Predicate toPredicate(@NonNull Root<Product> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
                if (maxPrice == null) {
                    return criteriaBuilder.conjunction();
                }
                return criteriaBuilder.lessThanOrEqualTo(root.get("basePrice"), maxPrice);
            }
        };
    }

    public static Specification<Product> equalCategoryId(String categoryId) {
        return (root, query, criteriaBuilder) -> {
            if (categoryId == null || categoryId.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("category").get("id"), categoryId);
        };
    }
}
