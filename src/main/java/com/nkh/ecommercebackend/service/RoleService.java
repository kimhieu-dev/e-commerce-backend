package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.dto.request.RoleReq;
import com.nkh.ecommercebackend.entity.Role;
import com.nkh.ecommercebackend.entity.User;

import java.util.List;

public interface RoleService {
    Role createRole(RoleReq roleReq);
}
