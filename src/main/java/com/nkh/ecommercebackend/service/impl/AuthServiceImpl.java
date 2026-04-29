package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.dto.request.LoginReq;
import com.nkh.ecommercebackend.dto.request.RegisterUserReq;
import com.nkh.ecommercebackend.service.AuthService;
import com.nkh.ecommercebackend.service.UserRoleService;
import com.nkh.ecommercebackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRoleService userRoleService;
    private final UserService userService;

    @Override
    public void registerUser(RegisterUserReq userReq) {
        userRoleService.createUser(userReq);
    }

    @Override
    public Boolean login(LoginReq request) {
        Boolean result = userService.checkIfUsernameExists(request.getUsername());
        return result;
    }
}
