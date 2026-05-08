package com.nkh.ecommercebackend.dto.response;

import com.nkh.ecommercebackend.common.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountRes implements Serializable {
    private String code;
    private DiscountType type;
    private BigDecimal value;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer usageLimit;
    private Integer usedCount;

}
