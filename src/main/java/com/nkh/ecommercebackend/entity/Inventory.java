package com.nkh.ecommercebackend.entity;

import com.nkh.ecommercebackend.common.InventoryStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "inventory")
public class Inventory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id",unique = true, nullable = false)
    private String id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;

    @Column(name = "quantity_in_stock",nullable = false)
    private Integer quantityInStock;

    @Column(name = "reserved_quantity",nullable = false)
    private Integer reservedQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private InventoryStatus status;

}
