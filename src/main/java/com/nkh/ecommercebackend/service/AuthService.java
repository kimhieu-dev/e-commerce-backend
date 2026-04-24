package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.dto.request.RegisterUserReq;
import com.nkh.ecommercebackend.dto.request.UserReq;

public interface AuthService {
    void registerUser(RegisterUserReq userReq);
}
