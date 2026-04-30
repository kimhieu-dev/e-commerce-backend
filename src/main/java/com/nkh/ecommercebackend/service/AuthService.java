package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.dto.request.LoginReq;
import com.nkh.ecommercebackend.dto.request.RegisterUserReq;
import com.nkh.ecommercebackend.dto.response.LoginRes;

public interface AuthService {
    void register(RegisterUserReq userReq);

    LoginRes login(LoginReq request);
}
