package com.nkh.ecommercebackend.mapper;

import com.nkh.ecommercebackend.dto.request.RoleReq;
import com.nkh.ecommercebackend.dto.response.RoleRes;
import com.nkh.ecommercebackend.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toRole(RoleReq roleReq);
    RoleRes toRoleRes(Role role);
}
