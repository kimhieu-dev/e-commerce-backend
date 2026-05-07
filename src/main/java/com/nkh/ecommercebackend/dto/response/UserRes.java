package com.nkh.ecommercebackend.dto.response;

import com.nkh.ecommercebackend.common.Gender;
import com.nkh.ecommercebackend.common.UserStatus;
import com.nkh.ecommercebackend.entity.Cart;
import com.nkh.ecommercebackend.entity.Role;
import com.nkh.ecommercebackend.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRes implements Serializable {
    private String username;

    private String email;

    private String phoneNumber;

    private String fullName;

    private Gender gender;

    private LocalDate dateBirth;

    private UserStatus status;

    private Boolean isVerified;

    //private List<Role> roles;
}
