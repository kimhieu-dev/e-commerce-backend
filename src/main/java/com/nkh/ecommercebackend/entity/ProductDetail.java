package com.nkh.ecommercebackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_detail")
public class ProductDetail extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "weight", nullable = false)
    private BigDecimal weight;

    @Column(name = "length", nullable = false)
    private BigDecimal length;

    @Column(name = "width", nullable = false)
    private BigDecimal width;

    @Column(name = "height", nullable = false)
    private BigDecimal height;


}
