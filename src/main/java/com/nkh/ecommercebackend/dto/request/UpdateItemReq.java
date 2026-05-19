package com.nkh.ecommercebackend.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateItemReq implements Serializable {
    @Min(value = 1,message = "quantity must greater than or equals to 1")
    private int quantity;
}
