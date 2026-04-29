package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.dto.request.LoginReq;
import com.nkh.ecommercebackend.dto.request.RegisterUserReq;
import com.nkh.ecommercebackend.dto.request.UserReq;
import jakarta.validation.Valid;

public interface AuthService {
    void registerUser(RegisterUserReq userReq);

    Boolean login( LoginReq request);
}
