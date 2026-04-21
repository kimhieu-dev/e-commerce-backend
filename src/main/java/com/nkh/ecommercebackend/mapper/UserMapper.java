package com.nkh.ecommercebackend.mapper;

import com.nkh.ecommercebackend.dto.request.UserReq;
import com.nkh.ecommercebackend.dto.response.UserRes;
import com.nkh.ecommercebackend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserRes toUserRes(User user);
    void updateUser(@MappingTarget User user, UserReq userReq);
}
