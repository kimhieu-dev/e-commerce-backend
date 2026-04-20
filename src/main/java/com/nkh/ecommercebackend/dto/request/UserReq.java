package com.nkh.ecommercebackend.dto.request;

import com.nkh.ecommercebackend.common.Gender;
import com.nkh.ecommercebackend.common.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserReq implements Serializable {

    private String username;

    private String password;

    private String email;

    private String phoneNumber;

    private String fullName;

    private Gender gender;

    private Date dateBirth;

}
