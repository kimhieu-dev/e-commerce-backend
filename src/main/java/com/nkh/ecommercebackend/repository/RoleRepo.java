package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role,String> {
}
