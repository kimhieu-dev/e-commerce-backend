package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.dto.request.CreateUserReq;
import com.nkh.ecommercebackend.dto.request.UserFilterReq;
import com.nkh.ecommercebackend.dto.response.UserRes;
import com.nkh.ecommercebackend.entity.Role;
import com.nkh.ecommercebackend.entity.User;
import com.nkh.ecommercebackend.entity.UserRole;
import com.nkh.ecommercebackend.exception.BusinessException;
import com.nkh.ecommercebackend.exception.ErrorCode;
import com.nkh.ecommercebackend.mapper.UserMapper;
import com.nkh.ecommercebackend.repository.UserRepo;
import com.nkh.ecommercebackend.repository.UserRoleRepo;
import com.nkh.ecommercebackend.service.RoleService;
import com.nkh.ecommercebackend.service.UserService;
import com.nkh.ecommercebackend.service.spec.UserSpec;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final RoleService roleService;
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(CreateUserReq request) {
        Boolean checkUsername = userRepo.existsByUsername(request.getUsername());
        if (checkUsername) {
            throw new BusinessException(ErrorCode.USER_EXISTED);
        }
        Boolean checkEmail = userRepo.existsByEmail(request.getEmail());
        if (checkEmail) {
            throw new BusinessException(ErrorCode.EMAIL_EXISTED);
        }
        Boolean checkPhoneNumber = userRepo.existsByPhoneNumber(request.getPhoneNumber());
        if (checkPhoneNumber) {
            throw new BusinessException(ErrorCode.PHONE_NUMBER_EXISTED);
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        log.info("User created successfully");
        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        Optional<User> userOptional = userRepo.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        return userOptional.get();
    }

    @Override
    public Optional<User> checkIfUsernameExists(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public List<UserRes> getUsers(UserFilterReq request, Pageable pageable) {
        Specification<User> specification = (root, query, cb) -> cb.conjunction();
        if (request.getUsername() != null && !request.getUsername().isEmpty()) {
            specification = specification.and(UserSpec.likeUsername(request.getUsername()));
        }
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            specification = specification.and(UserSpec.likeEmail(request.getEmail()));
        }
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
            specification = specification.and(UserSpec.equalPhoneNumber(request.getPhoneNumber()));
        }
        if (request.getFullName() != null && !request.getFullName().isEmpty()) {
            specification = specification.and(UserSpec.likeFullName(request.getFullName()));
        }
        if (request.getGender() != null) {
            specification = specification.and(UserSpec.equalGender(request.getGender()));
        }
        if (request.getDateBirthFrom() != null) {
            specification = specification.and(UserSpec.equalDateBirthFrom(request.getDateBirthFrom()));
        }
        if (request.getDateBirthTo() != null) {
            specification = specification.and(UserSpec.equalDateBirthTo(request.getDateBirthTo()));
        }
        if (request.getStatus() != null) {
            specification = specification.and(UserSpec.equalStatus(request.getStatus()));
        }
        if (request.getRole() != null) {
            specification = specification.and(UserSpec.equalRole(request.getRole()));
        }
        if (pageable == null) {
            pageable = Pageable.unpaged();
        }
        Page<User> users = userRepo.findAll(specification, pageable);
        List<User> userList = users.getContent();
        List<UserRes> userResList = new ArrayList<>();
        for (User user : userList) {
            UserRes userRes = new UserRes();
            userRes.setUsername(user.getUsername());
            userRes.setPassword(user.getPassword());
            userRes.setEmail(user.getEmail());
            userRes.setPhoneNumber(user.getPhoneNumber());
            userRes.setFullName(user.getFullName());
            userRes.setGender(user.getGender());
            userRes.setStatus(user.getStatus());
            userRes.setDateBirth(user.getDateBirth());
            userRes.setIsVerified(user.getIsVerified());
            userResList.add(userRes);
        }
        return userResList;
    }
}
