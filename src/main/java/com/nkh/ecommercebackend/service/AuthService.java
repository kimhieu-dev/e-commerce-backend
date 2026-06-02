package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.dto.request.*;
import com.nkh.ecommercebackend.dto.response.IntrospectRes;
import com.nkh.ecommercebackend.dto.response.LoginRes;

public interface AuthService {
    void register(RegisterReq userReq);

    LoginRes login(LoginReq request);

    IntrospectRes introspect(IntrospectReq request);

    void logout(LogoutReq request);

    LoginRes refreshToken(RefreshTokenReq request);

    void forgotPassword(ForgotPasswordReq request);
}
