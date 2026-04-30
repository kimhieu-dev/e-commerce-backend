package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.dto.request.LoginReq;
import com.nkh.ecommercebackend.dto.request.RegisterUserReq;
import com.nkh.ecommercebackend.entity.User;
import com.nkh.ecommercebackend.exception.BusinessException;
import com.nkh.ecommercebackend.exception.ErrorCode;
import com.nkh.ecommercebackend.service.AuthService;
import com.nkh.ecommercebackend.service.UserRoleService;
import com.nkh.ecommercebackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRoleService userRoleService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(RegisterUserReq userReq) {
        userRoleService.createUser(userReq);
    }

    @Override
    public Boolean login(LoginReq request) {
        Optional<User> userOptional = userService.checkIfUsernameExists(request.getUsername());
        if (userOptional.isEmpty()) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        return passwordEncoder.matches(request.getPassword(), userOptional.get().getPassword());
    }
}
