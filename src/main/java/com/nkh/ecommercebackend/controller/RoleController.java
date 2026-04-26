package com.nkh.ecommercebackend.controller;

import com.nkh.ecommercebackend.dto.request.RoleReq;
import com.nkh.ecommercebackend.dto.response.RoleRes;
import com.nkh.ecommercebackend.entity.Role;

import com.nkh.ecommercebackend.mapper.RoleMapper;
import com.nkh.ecommercebackend.service.RoleService;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Validated
public class RoleController {
    private final RoleService roleService;
    private final RoleMapper roleMapper;

    @PostMapping
    public ResponseEntity<RoleRes> createRole(@RequestBody @Valid RoleReq roleReq) {
        Role role = roleService.createRole(roleReq);
        RoleRes response = roleMapper.toRoleRes(role);
        return ResponseEntity.ok(response);
    }
}
