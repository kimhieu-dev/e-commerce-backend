package com.nkh.ecommercebackend.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CreateCategoryReq implements Serializable {
    private String parentId;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
}
