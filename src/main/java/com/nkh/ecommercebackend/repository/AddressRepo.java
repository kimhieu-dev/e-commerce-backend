package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.lang.ScopedValue;
import java.util.List;
import java.util.Optional;

public interface AddressRepo extends JpaRepository<Address, String> {

    @Query("""
                select a from Address a join fetch a.user u where u.username = :username
            """)
    List<Address> findAllByUsername(String username);

    Optional<Address> findByIdAndUserId(String id, String userId);
}
