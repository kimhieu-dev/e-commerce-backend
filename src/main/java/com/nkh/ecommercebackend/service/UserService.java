package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.dto.request.UserReq;
import com.nkh.ecommercebackend.entity.User;

import java.util.Optional;

public interface UserService {
    User createUser(UserReq request);
    User getUserByUsername(String username);
    Optional<User> checkIfUsernameExists(String username);
}
