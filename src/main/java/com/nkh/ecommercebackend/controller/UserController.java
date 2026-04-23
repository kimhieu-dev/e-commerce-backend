package com.nkh.ecommercebackend.controller;

import com.nkh.ecommercebackend.dto.request.UserReq;
import com.nkh.ecommercebackend.dto.response.UserRes;
import com.nkh.ecommercebackend.entity.User;
import com.nkh.ecommercebackend.mapper.UserMapper;
import com.nkh.ecommercebackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserRes> createUser(@RequestBody UserReq request){
        User user = userService.createUser(request);
        UserRes response = userMapper.toUserRes(user);
        return ResponseEntity.ok(response);
    }
}
