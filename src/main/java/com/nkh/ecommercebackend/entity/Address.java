package com.nkh.ecommercebackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "address")
public class Address extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "recipient_name",nullable = false)
    private String recipientName;

    @Column(name = "phone_number",nullable = false)
    private String phoneNumber;

    @Column(name = "province",nullable = false)
    private String province;

    @Column(name = "district",nullable = false)
    private String district;

    @Column(name = "ward",nullable = false)
    private String ward;

    @Column(name = "detail_address",nullable = false)
    private String detailAddress;

}
