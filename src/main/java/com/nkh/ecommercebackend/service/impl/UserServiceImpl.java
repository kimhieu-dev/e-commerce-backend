package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.config.PasswordEncoderConfig;
import com.nkh.ecommercebackend.dto.request.UserReq;
import com.nkh.ecommercebackend.entity.User;
import com.nkh.ecommercebackend.exception.BusinessException;
import com.nkh.ecommercebackend.exception.ErrorCode;
import com.nkh.ecommercebackend.mapper.UserMapper;
import com.nkh.ecommercebackend.repository.UserRepo;
import com.nkh.ecommercebackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(UserReq request) {
        Optional<User> userOptional = userRepo.findByUsername(request.getUsername());
        if(userOptional.isPresent()){
            throw new BusinessException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return user;
    }
}
