package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.dto.request.RegisterUserReq;

public interface UserRoleService {
    void createUser(RegisterUserReq request);
}
