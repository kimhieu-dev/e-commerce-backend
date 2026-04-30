package com.nkh.ecommercebackend.controller;

import com.nkh.ecommercebackend.dto.request.IntrospectReq;
import com.nkh.ecommercebackend.dto.request.LoginReq;
import com.nkh.ecommercebackend.dto.request.RegisterUserReq;
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
    public BaseResponse<?> register(@RequestBody @Valid RegisterUserReq request){
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
//
//    @PostMapping("/logout")
//    ApiResponse<Void> logout(@RequestBody @Valid LogoutRequest logoutRequest) {
//        authenticationService.logout(logoutRequest);
//        return ApiResponse.<Void>builder()
//                .message("Logout successful")
//                .build();
//    }

}
