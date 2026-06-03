package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedTokenRepo extends JpaRepository<InvalidatedToken, String> {
}
