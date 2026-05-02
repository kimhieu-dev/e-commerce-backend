package com.nkh.ecommercebackend.service.spec;

import com.nkh.ecommercebackend.common.Gender;
import com.nkh.ecommercebackend.common.Role;
import com.nkh.ecommercebackend.common.UserStatus;
import com.nkh.ecommercebackend.entity.User;
import com.nkh.ecommercebackend.entity.UserRole;
import jakarta.persistence.criteria.*;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class UserSpec {
    public static Specification<User> likeUsername(String username) {
        return new Specification<User>() {
            @Override
            public @Nullable Predicate toPredicate(@NonNull Root<User> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
                if (username == null || username.isEmpty()) {
                    return criteriaBuilder.conjunction();
                }
                return criteriaBuilder.like(root.get("username"), "%" + username + "%");
            }
        };
    }

    public static Specification<User> likeEmail(String email) {
        return new Specification<User>() {
            @Override
            public @Nullable Predicate toPredicate(@NonNull Root<User> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
                if (email == null || email.isEmpty()) {
                    return criteriaBuilder.conjunction();
                }
                return criteriaBuilder.like(root.get("email"), "%" + email + "%");
            }
        };
    }

    public static Specification<User> equalPhoneNumber(String phoneNumber) {
        return new Specification<User>() {
            @Override
            public @Nullable Predicate toPredicate(@NonNull Root<User> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
                if (phoneNumber == null || phoneNumber.isEmpty()) {
                    return criteriaBuilder.conjunction();
                }
                return criteriaBuilder.equal(root.get("phoneNumber"), phoneNumber);
            }
        };
    }

    public static Specification<User> likeFullName(String fullName) {
        return new Specification<User>() {
            @Override
            public @Nullable Predicate toPredicate(@NonNull Root<User> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
                if (fullName == null || fullName.isEmpty()) {
                    return criteriaBuilder.conjunction();
                }
                return criteriaBuilder.like(root.get("fullName"), "%" + fullName + "%");
            }
        };
    }

    public static Specification<User> equalGender(Gender gender) {
        return new Specification<User>() {
            @Override
            public @Nullable Predicate toPredicate(@NonNull Root<User> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
                if (gender == null) {
                    return criteriaBuilder.conjunction();
                }
                return criteriaBuilder.equal(root.get("gender"), gender);
            }
        };
    }

    public static Specification<User> equalDateBirthFrom(LocalDate dateBirthFrom) {
        return new Specification<User>() {
            @Override
            public @Nullable Predicate toPredicate(@NonNull Root<User> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
                if (dateBirthFrom == null) {
                    return criteriaBuilder.conjunction();
                }
                return criteriaBuilder.greaterThanOrEqualTo(root.get("dateBirth"), dateBirthFrom);
            }
        };
    }

    public static Specification<User> equalDateBirthTo(LocalDate dateBirthTo) {
        return new Specification<User>() {
            @Override
            public @Nullable Predicate toPredicate(@NonNull Root<User> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
                if (dateBirthTo == null) {
                    return criteriaBuilder.conjunction();
                }
                return criteriaBuilder.lessThanOrEqualTo(root.get("dateBirth"), dateBirthTo);
            }
        };
    }

    public static Specification<User> equalStatus(UserStatus status) {
        return new Specification<User>() {
            @Override
            public @Nullable Predicate toPredicate(@NonNull Root<User> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
                if (status == null) {
                    return criteriaBuilder.conjunction();
                }
                return criteriaBuilder.equal(root.get("status"), status);
            }
        };
    }

    public static Specification<User> equalRole(Role role) {
        return new Specification<User>() {
            @Override
            public @Nullable Predicate toPredicate(@NonNull Root<User> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
                if (role == null) {
                    return criteriaBuilder.conjunction();
                }
                Join<User, UserRole> userRoleJoin = root.join("userRoles", JoinType.LEFT);
                Join<UserRole, Role> roleJoin = userRoleJoin.join("role", JoinType.LEFT);
                return criteriaBuilder.equal(roleJoin.get("role"), role);
            }
        };
    }
}
