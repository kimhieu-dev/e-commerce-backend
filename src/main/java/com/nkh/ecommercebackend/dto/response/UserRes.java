package com.nkh.ecommercebackend.dto.response;

import com.nkh.ecommercebackend.common.Gender;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class UserRes implements Serializable {
    private String username;

    private String password;

    private String email;

    private String phoneNumber;

    private String fullName;

    private Gender gender;

    private Date dateBirth;
}
