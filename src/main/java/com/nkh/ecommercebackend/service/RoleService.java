package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.dto.request.RoleReq;
import com.nkh.ecommercebackend.entity.Role;

public interface RoleService {
    Role createRole(RoleReq roleReq);
}
