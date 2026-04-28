package com.nkh.ecommercebackend.entity;

import com.nkh.ecommercebackend.common.ProductStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "sku", nullable = false)
    private String sku;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "base_price",nullable = false)
    private BigDecimal basePrice;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "status")
    private ProductStatus status;

    @OneToOne(mappedBy = "product",cascade = CascadeType.ALL)
    private ProductDetail productDetail;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private List<CartItem> cartItems;
}
