package com.nkh.ecommercebackend.repository;

import com.nkh.ecommercebackend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ProductRepo extends JpaRepository<Product,String>, JpaSpecificationExecutor<Product> {
    Boolean existsBySku(String sku);

    @Query("""
            select sum(p.basePrice * i.quantityInStock) from Product p
                        join p.inventory i where i.status in (InventoryStatus.IN_STOCK, InventoryStatus.LIMITED_STOCK)
                                   and i.updatedAt between :fromDateTime and :toDateTime
            """)
    BigDecimal getInventoryValue(LocalDateTime fromDateTime, LocalDateTime toDateTime);

    @Query("""
                    select count(p.id) from Product p
                                where p.createdAt between :fromDateTime and :toDateTime and p.status = ProductStatus.ACTIVE
            """)
    Integer getTotalProducts(LocalDateTime fromDateTime, LocalDateTime toDateTime);

    @Query("""
            select count(p.id) from Product p
                        join p.inventory i where i.status = InventoryStatus.LIMITED_STOCK
                                   and i.updatedAt between :fromDateTime and :toDateTime
            """)
    Integer getTotalLimitedStock(LocalDateTime fromDateTime, LocalDateTime toDateTime);
}
