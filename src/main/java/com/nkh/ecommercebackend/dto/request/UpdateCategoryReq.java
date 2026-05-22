package com.nkh.ecommercebackend.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryReq implements Serializable {
    @NotEmpty
    private String id;

    private String parentId;

    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
}
