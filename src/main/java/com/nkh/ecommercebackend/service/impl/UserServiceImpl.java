package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.dto.request.UserReq;
import com.nkh.ecommercebackend.entity.User;
import com.nkh.ecommercebackend.exception.BusinessException;
import com.nkh.ecommercebackend.exception.ErrorCode;
import com.nkh.ecommercebackend.mapper.UserMapper;
import com.nkh.ecommercebackend.repository.UserRepo;
import com.nkh.ecommercebackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(UserReq request) {
        Boolean checkUsername = userRepo.existsByUsername(request.getUsername());
        if(checkUsername){
            throw new BusinessException(ErrorCode.USER_EXISTED);
        }
        Boolean checkEmail = userRepo.existsByEmail(request.getEmail());
        if(checkEmail){
            throw new BusinessException(ErrorCode.EMAIL_EXISTED);
        }
        Boolean checkPhoneNumber = userRepo.existsByPhoneNumber(request.getPhoneNumber());
        if(checkPhoneNumber){
            throw new BusinessException(ErrorCode.PHONE_NUMBER_EXISTED);
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        log.info("User created successfully");
        return user;
    }
}
