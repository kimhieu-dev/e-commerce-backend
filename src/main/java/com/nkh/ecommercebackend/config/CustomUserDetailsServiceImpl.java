package com.nkh.ecommercebackend.config;

import com.nkh.ecommercebackend.repository.UserRepo;
import com.nkh.ecommercebackend.repository.UserRoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private final UserRepo userRepo;
    private final UserRoleRepo userRoleRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.nkh.ecommercebackend.entity.User> userOptional = userRepo.findByUsername(username);
        if(userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Username not found");
        }
//        String role = "ROLE_USER";
//        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
        return new User(username,userOptional.get().getPassword(), List.of());

    }
}
