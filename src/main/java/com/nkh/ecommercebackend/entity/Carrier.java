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
@Table(name = "carriers")
public class Carrier extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, unique = true)
    private String id;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "base_price",nullable = false)
    private BigDecimal basePrice;

    @Column(name = "estimated_days", nullable = false)
    private Integer estimatedDays;

}
