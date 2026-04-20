package com.nkh.ecommercebackend.dto.request;

import com.nkh.ecommercebackend.common.Gender;
import com.nkh.ecommercebackend.common.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @NotBlank
    @Size(min = 6, max = 12,message = "Username less than 50")
    private String username;

    @NotBlank
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")
    //min 8 , contain uppercase, lowercase, number and special character
    private String password;

    @Email
    @NotBlank
    private String email;

    @Pattern(regexp = "^[0-9]{10}$")
    private String phoneNumber;

    @NotBlank
    @Size(max = 100)
    private String fullName;

    @NotNull
    private Gender gender;

    @Past
    private Date dateBirth;

}
