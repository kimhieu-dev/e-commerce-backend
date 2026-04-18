package com.nkh.ecommercebackend.entity;

import com.nkh.ecommercebackend.common.Gender;
import com.nkh.ecommercebackend.common.Status;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private Integer id;

    @Column(name = "username",updatable = true, nullable = false, unique = true)
    private String username;

    private String password;

    private String email;

    private String phoneNumber;

    private String fullName;

    private Gender gender;

    private Date dateBirth;

    private Status status;

    private Boolean isVerified;
}
