package com.nkh.ecommercebackend.dto.response;

import com.nkh.ecommercebackend.common.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRes implements Serializable {
    private String username;

    private String password;

    private String email;

    private String phoneNumber;

    private String fullName;

    private Gender gender;

    private Date dateBirth;
}
