package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.dto.request.RoleReq;
import com.nkh.ecommercebackend.entity.Role;
import com.nkh.ecommercebackend.entity.User;
import com.nkh.ecommercebackend.entity.UserRole;
import com.nkh.ecommercebackend.exception.BusinessException;
import com.nkh.ecommercebackend.exception.ErrorCode;
import com.nkh.ecommercebackend.mapper.RoleMapper;
import com.nkh.ecommercebackend.repository.RoleRepo;
import com.nkh.ecommercebackend.repository.UserRepo;
import com.nkh.ecommercebackend.repository.UserRoleRepo;
import com.nkh.ecommercebackend.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepo roleRepo;
    private final UserRoleRepo userRoleRepo;
    private final RoleMapper roleMapper;

    @Override
    public Role createRole(RoleReq request) {
        Boolean checkRole = roleRepo.existsByName(request.getName());
        if (checkRole) {
            throw new BusinessException(ErrorCode.ROLE_EXISTED);
        }
        Role role = roleMapper.toRole(request);
        roleRepo.save(role);
        return role;
    }


}
