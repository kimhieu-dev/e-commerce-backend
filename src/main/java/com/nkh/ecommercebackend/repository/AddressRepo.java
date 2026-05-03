package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<Address,String> {
}
