package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role,String> {
    Optional<Role> findByName(String name);

    Boolean existsByName(String name);
}
