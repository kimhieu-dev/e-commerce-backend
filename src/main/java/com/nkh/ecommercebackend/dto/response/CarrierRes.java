package com.nkh.ecommercebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarrierRes implements Serializable {
    private String name;
    private BigDecimal basePrice;
    private BigDecimal tax;
    private Integer estimatedDays;
}
