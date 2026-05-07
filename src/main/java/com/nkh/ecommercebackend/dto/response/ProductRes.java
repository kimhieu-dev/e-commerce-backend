package com.nkh.ecommercebackend.dto.response;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRes implements Serializable {
    private String id;

    private String sku;

    private String name;

    private BigDecimal basePrice;

    private String thumbnailUrl;

    private InventoryRes inventory;
}
