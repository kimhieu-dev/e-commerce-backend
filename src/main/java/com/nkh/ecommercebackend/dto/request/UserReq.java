package com.nkh.ecommercebackend.dto.request;

import com.nkh.ecommercebackend.common.Gender;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserReq implements Serializable {

    @NotBlank(message = "Username is blank")
    @Size(min = 8, max = 12, message = "Username must be at minimum 8 and maximum 12")
    private String username;

    @NotBlank(message = "Your password is blank ")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")
    //min 8 , contain uppercase, lowercase, number and special character
    private String password;

    @Email(message = "Email is not valid")
    @NotBlank(message = "Email is blank")
    private String email;

    @Pattern(regexp = "^[0-9]{10}$")
    private String phoneNumber;

    @NotBlank(message = "Your full name is blank")
    @Size(max = 100,message = "Maximum size: 100")
    private String fullName;

    @NotNull(message = "Gender is not valid")
    private Gender gender;

    @Past(message = "Please enter an valid Date of birth")
    private LocalDate dateBirth;

}
