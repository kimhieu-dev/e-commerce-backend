package com.nkh.ecommercebackend.dto.response;

import com.nkh.ecommercebackend.entity.Cart;
import com.nkh.ecommercebackend.entity.Product;
import jakarta.persistence.*;
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
public class CartItemRes implements Serializable {
    private String id;

    private Product product;

    private Integer quantity;

    private BigDecimal unitPrice;
}
