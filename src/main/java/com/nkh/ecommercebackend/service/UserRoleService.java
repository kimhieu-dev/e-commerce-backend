package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.dto.request.RegisterReq;
import com.nkh.ecommercebackend.entity.User;
import com.nkh.ecommercebackend.entity.UserRole;

import java.util.List;

public interface UserRoleService {
    void createUser(RegisterReq request);
    List<UserRole> getRoles(List<User> users);
}
