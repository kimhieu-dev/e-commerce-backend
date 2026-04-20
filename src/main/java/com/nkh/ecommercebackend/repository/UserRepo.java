package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Integer> {
}
