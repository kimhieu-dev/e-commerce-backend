package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.dto.request.IntrospectReq;
import com.nkh.ecommercebackend.dto.request.LoginReq;
import com.nkh.ecommercebackend.dto.request.RegisterReq;
import com.nkh.ecommercebackend.dto.response.IntrospectRes;
import com.nkh.ecommercebackend.dto.response.LoginRes;

public interface AuthService {
    void register(RegisterReq userReq);

    LoginRes login(LoginReq request);

    IntrospectRes introspect(IntrospectReq request);
}
