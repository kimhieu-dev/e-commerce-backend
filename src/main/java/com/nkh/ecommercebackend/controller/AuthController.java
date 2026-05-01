package com.nkh.ecommercebackend.controller;

import com.nkh.ecommercebackend.dto.request.*;
import com.nkh.ecommercebackend.dto.response.BaseResponse;
import com.nkh.ecommercebackend.dto.response.IntrospectRes;
import com.nkh.ecommercebackend.dto.response.LoginRes;
import com.nkh.ecommercebackend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public BaseResponse<?> register(@RequestBody @Valid RegisterReq request){
        authService.register(request);
        return BaseResponse.success("Register Successfully");
    }

    @PostMapping("/login")
    public BaseResponse<LoginRes> login(@RequestBody @Valid LoginReq request){
        return BaseResponse.success(authService.login(request));
    }

    @PostMapping("/introspect")
    BaseResponse<IntrospectRes> introspect(@RequestBody @Valid IntrospectReq request) {
        return BaseResponse.success(authService.introspect(request));
    }

    @PostMapping("/logout")
    BaseResponse<?> logout(@RequestBody @Valid LogoutReq request) {
        //authService.logout(request);
        return BaseResponse.success("Logout successfully");
    }

    @PostMapping("/refresh-token")
    BaseResponse<?> logout(@RequestBody @Valid RefreshTokenReq request) {
        //authService.logout(request);
        return BaseResponse.success("Logout successfully");
    }

}
