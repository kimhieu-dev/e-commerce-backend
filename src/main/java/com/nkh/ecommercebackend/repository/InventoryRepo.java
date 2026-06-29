package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface InventoryRepo extends JpaRepository <Inventory,String> {

    @Query("SELECT i FROM Inventory i WHERE i.product.id IN :productIds")
    List<Inventory> findByProductIdIn( List<String> productIds);
}
