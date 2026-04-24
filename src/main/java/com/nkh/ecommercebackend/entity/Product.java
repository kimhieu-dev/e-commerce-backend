package com.nkh.ecommercebackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Table(name = "product")
public class Product extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    //@Column(name = "id")
    private UUID id;

    private String name;

    private String description;

    private BigDecimal basePrice;

    private String thumbnailUrl;

    @OneToOne(mappedBy = "product")
    private ProductDetail productDetail;
}
