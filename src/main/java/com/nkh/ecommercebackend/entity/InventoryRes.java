package com.nkh.ecommercebackend.entity;

import com.nkh.ecommercebackend.common.InventoryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRes implements Serializable {
    private Integer quantityInStock;
    private Integer reservedQuantity;
    private InventoryStatus status;
}
