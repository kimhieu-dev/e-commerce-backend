package com.nkh.ecommercebackend.controller;

import com.nkh.ecommercebackend.dto.request.LoginReq;
import com.nkh.ecommercebackend.dto.request.RegisterUserReq;
import com.nkh.ecommercebackend.dto.response.BaseResponse;
import com.nkh.ecommercebackend.dto.response.LoginRes;
import com.nkh.ecommercebackend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterUserReq request){
        authService.registerUser(request);
        return ResponseEntity.ok("Register Successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginRes>> login(@RequestBody @Valid LoginReq request){
        Boolean authenticated = authService.login(request);
        LoginRes loginRes = new LoginRes();
        loginRes.setAuthenticated(authenticated);
        return ResponseEntity.ok(BaseResponse.success(loginRes));
    }
}
