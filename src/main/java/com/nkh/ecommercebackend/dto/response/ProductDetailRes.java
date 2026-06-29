package com.nkh.ecommercebackend.dto.response;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetailRes implements Serializable {
    private String id;
    private String sku;
    private String name;
    private BigDecimal basePrice;
    private String thumbnailUrl;
    private String categoryId;
    private String categoryName;
    private InventoryRes inventory;
    private String description;
    private BigDecimal weight;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;
}
