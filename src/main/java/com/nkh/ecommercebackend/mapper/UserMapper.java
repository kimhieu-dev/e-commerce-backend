package com.nkh.ecommercebackend.mapper;

import com.nkh.ecommercebackend.dto.request.CreateUserReq;
import com.nkh.ecommercebackend.dto.request.RegisterReq;
import com.nkh.ecommercebackend.dto.response.CreateUserRes;
import com.nkh.ecommercebackend.dto.response.UserRes;
import com.nkh.ecommercebackend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    CreateUserRes toUserRes(User user);

    void updateUser(@MappingTarget User user, CreateUserReq request);

    User toUser(CreateUserReq request);

    User toUser(RegisterReq request);

    List<UserRes> toListUserRes(List<User> users);
}
