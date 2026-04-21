package com.nkh.ecommercebackend.entity;

import com.nkh.ecommercebackend.common.Gender;
import com.nkh.ecommercebackend.common.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private UUID id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email",nullable = false)
    private String email;

    @Column(name = "phone_number",nullable = false)
    private String phoneNumber;

    @Column(name = "full_name",nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender",nullable = false)
    private Gender gender;

    @Column(name = "date_birth",nullable = false)
    private Date dateBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private UserStatus status;

    @Column(name = "is_verified",nullable = false)
    private Boolean isVerified;

    @OneToOne(mappedBy = "user")
    private Cart cart;

}
