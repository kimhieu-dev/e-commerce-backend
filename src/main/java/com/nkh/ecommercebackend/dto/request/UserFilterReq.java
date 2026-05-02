package com.nkh.ecommercebackend.dto.request;

import com.nkh.ecommercebackend.common.Gender;
import com.nkh.ecommercebackend.common.Role;
import com.nkh.ecommercebackend.common.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFilterReq implements Serializable {

    private String username;

    private String email;
    private String phoneNumber;
    private String fullName;
    private Gender gender;
    private LocalDate dateBirthFrom;
    private LocalDate dateBirthTo;
    private UserStatus status;
    private Role role;
}
