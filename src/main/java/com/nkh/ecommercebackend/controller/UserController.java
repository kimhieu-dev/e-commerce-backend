package com.nkh.ecommercebackend.controller;

import com.nkh.ecommercebackend.dto.request.CreateUserReq;
import com.nkh.ecommercebackend.dto.request.UserFilterReq;
import com.nkh.ecommercebackend.dto.response.UserRes;
import com.nkh.ecommercebackend.dto.response.BaseResponse;
import com.nkh.ecommercebackend.dto.response.CreateUserRes;
import com.nkh.ecommercebackend.entity.User;
import com.nkh.ecommercebackend.entity.UserRole;
import com.nkh.ecommercebackend.mapper.UserMapper;
import com.nkh.ecommercebackend.repository.UserRepo;
import com.nkh.ecommercebackend.service.UserRoleService;
import com.nkh.ecommercebackend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;
    private final UserRoleService userRoleService;
    private final UserMapper userMapper;
    private final UserRepo userRepo;

    @PostMapping
    public ResponseEntity<CreateUserRes> createUser(@RequestBody @Valid CreateUserReq request){
        User user = userService.createUser(request);
        CreateUserRes response = userMapper.toUserRes(user);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public BaseResponse<List<UserRes>> getUsers(UserFilterReq request, Pageable pageable){
        List<UserRes> response = userService.getUsers(request,pageable);
        return BaseResponse.success(response);
    }
}
