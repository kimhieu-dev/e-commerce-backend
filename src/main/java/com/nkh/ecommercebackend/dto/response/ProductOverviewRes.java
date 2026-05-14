package com.nkh.ecommercebackend.dto.response;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOverviewRes implements Serializable {
    private BigDecimal inventoryValue;
    private Integer totalProducts;
    private Integer totalLimitedStock;
}
