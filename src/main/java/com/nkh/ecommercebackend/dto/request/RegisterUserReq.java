package com.nkh.ecommercebackend.dto.request;

import com.nkh.ecommercebackend.common.Gender;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserReq implements Serializable {
    @NotBlank(message = "USERNAME_BLANK")
    @Size(min = 8, max = 12, message = "USERNAME_INVALID")
    private String username;

    @NotBlank(message = "PASSWORD_BLANK")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "PASSWORD_INVALID")
    //min 8 , contain uppercase, lowercase, number and special character
    private String password;

    @Email(message = "EMAIL_INVALID")
    @NotBlank(message = "EMAIL_BLANK")
    private String email;

    @Pattern(regexp = "^[0-9]{10}$", message = "PHONE_NUMBER_INVALID")
    private String phoneNumber;

    @NotBlank(message = "FULL_NAME_BLANK")
    @Size(max = 100, message = "FULL_NAME_TOO_LONG")
    private String fullName;

    @NotNull(message = "GENDER_NULL")
    private Gender gender;

    @Past(message = "DATE_BIRTH_INVALID")
    private LocalDate dateBirth;

//    @NotNull(message = "ROLE_NULL")
//    private String roleId;
}
