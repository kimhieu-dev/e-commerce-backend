package com.nkh.ecommercebackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart_item")
public class CartItem extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;


    private Integer cartId;

    private Integer productId;

    private Integer quantity;
}
