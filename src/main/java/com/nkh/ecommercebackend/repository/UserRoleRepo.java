package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.entity.User;
import com.nkh.ecommercebackend.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepo extends JpaRepository<UserRole,String> {
    List<UserRole> findAllByUser(User user);
}
