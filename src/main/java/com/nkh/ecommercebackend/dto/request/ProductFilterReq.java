package com.nkh.ecommercebackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilterReq implements Serializable {
    private String sku;
    private String name;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String categoryId;
}
