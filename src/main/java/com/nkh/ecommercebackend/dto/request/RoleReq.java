package com.nkh.ecommercebackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleReq implements Serializable {
    @NotBlank(message = "ROLE_NAME_BLANK")
    private String name;
}
