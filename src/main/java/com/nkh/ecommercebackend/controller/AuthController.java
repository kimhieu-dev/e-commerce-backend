package com.nkh.ecommercebackend.controller;

import com.nkh.ecommercebackend.dto.request.RegisterUserReq;
import com.nkh.ecommercebackend.dto.request.UserReq;
import com.nkh.ecommercebackend.dto.response.UserRes;
import com.nkh.ecommercebackend.entity.User;
import com.nkh.ecommercebackend.mapper.UserMapper;
import com.nkh.ecommercebackend.service.AuthService;
import com.nkh.ecommercebackend.service.UserService;
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

}
