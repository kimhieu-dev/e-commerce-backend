package com.nkh.ecommercebackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
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
public class CreateCarrierReq implements Serializable {
    @NotBlank
    private String name;

    @Positive
    private BigDecimal basePrice;

    @Positive
    private Integer estimatedDays;
}
