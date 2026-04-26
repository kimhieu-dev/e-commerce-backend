package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.dto.request.RegisterUserReq;
import com.nkh.ecommercebackend.entity.Cart;
import com.nkh.ecommercebackend.entity.Role;
import com.nkh.ecommercebackend.entity.User;
import com.nkh.ecommercebackend.entity.UserRole;
import com.nkh.ecommercebackend.exception.BusinessException;
import com.nkh.ecommercebackend.exception.ErrorCode;
import com.nkh.ecommercebackend.mapper.UserMapper;
import com.nkh.ecommercebackend.repository.CartRepo;
import com.nkh.ecommercebackend.repository.RoleRepo;
import com.nkh.ecommercebackend.repository.UserRepo;
import com.nkh.ecommercebackend.repository.UserRoleRepo;
import com.nkh.ecommercebackend.service.UserRoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final UserRoleRepo userRoleRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final CartRepo cartRepo;

    @Override
    @Transactional
    public void createUser(RegisterUserReq request) {

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

        Cart cart = new Cart();
        cart.setUser(user);
        cartRepo.save(cart);

        Optional<Role> roleOptional = roleRepo.findByName("USER");
        if(roleOptional.isEmpty()){
            throw new BusinessException(ErrorCode.ROLE_NOT_FOUND);
        }

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(roleOptional.get());
        userRoleRepo.save(userRole);
    }
}
