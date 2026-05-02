package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepo extends JpaRepository <Inventory,String> {

}
