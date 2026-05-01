package com.nkh.ecommercebackend.service;

import com.nkh.ecommercebackend.dto.request.CreateUserReq;
import com.nkh.ecommercebackend.dto.request.UserFilterReq;
import com.nkh.ecommercebackend.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(CreateUserReq request);
    User getUserByUsername(String username);
    Optional<User> checkIfUsernameExists(String username);
    List<User> getUsers(UserFilterReq request, Pageable pageable);
}
