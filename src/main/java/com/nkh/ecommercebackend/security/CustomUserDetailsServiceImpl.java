package com.nkh.ecommercebackend.security;

import com.nkh.ecommercebackend.repository.UserRepo;
import com.nkh.ecommercebackend.repository.UserRoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private final UserRepo userRepo;
    private final UserRoleRepo userRoleRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.nkh.ecommercebackend.entity.User> userOptional = userRepo.findByUsernameWithRoles(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Username not found");
        }
        return new CustomUserDetails(userOptional.get());
    }
}
