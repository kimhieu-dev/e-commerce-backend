package com.nkh.ecommercebackend.dto.response;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRes implements Serializable {
    private String parentId;
    private String name;
    private String slug;
    private String description;
}
