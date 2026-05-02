package com.nkh.ecommercebackend.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import javax.crypto.Mac;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductReq implements Serializable {
    @NotBlank(message = "SKU_BLANK")
    private String sku;

    @NotBlank(message = "PRODUCT_NAME_BLANK")
    private String name;

    @NotNull(message = "BASE_PRICE_NULL")
    @DecimalMin(value = "0.0", inclusive = false, message = "BASE_PRICE_INVALID")
    private BigDecimal basePrice;

    private String thumbnailUrl;

    private String description;

    @NotNull(message = "WEIGHT_NULL")
    @DecimalMin(value = "0.0", inclusive = false, message = "WEIGHT_INVALID")
    private BigDecimal weight;

    @NotNull(message = "LENGTH_NULL")
    @DecimalMin(value = "0.0", inclusive = false, message = "LENGTH_INVALID")
    private BigDecimal length;

    @NotNull(message = "WIDTH_NULL")
    @DecimalMin(value = "0.0", inclusive = false, message = "WIDTH_INVALID")
    private BigDecimal width;

    @NotNull(message = "HEIGHT_NULL")
    @DecimalMin(value = "0.0", inclusive = false, message = "HEIGHT_INVALID")
    private BigDecimal height;

    @NotNull(message = "QUANTITY_IN_STOCK_NULL")
    @Min(value = 0, message = "QUANTITY_IN_STOCK_INVALID")
    private Integer quantityInStock;

    @NotNull(message = "RESERVED_QUANTITY_NULL")
    @Min(value = 0, message = "RESERVED_QUANTITY_INVALID")
    private Integer reservedQuantity;
}
