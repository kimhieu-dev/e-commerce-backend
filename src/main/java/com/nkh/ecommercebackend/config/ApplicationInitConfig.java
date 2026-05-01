package com.nkh.ecommercebackend.config;

import com.mysql.cj.conf.PropertyDefinitions;
import com.nkh.ecommercebackend.common.Gender;
import com.nkh.ecommercebackend.entity.Role;
import com.nkh.ecommercebackend.entity.User;
import com.nkh.ecommercebackend.entity.UserRole;
import com.nkh.ecommercebackend.repository.RoleRepo;
import com.nkh.ecommercebackend.repository.UserRepo;
import com.nkh.ecommercebackend.repository.UserRoleRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ApplicationInitConfig {
    private final PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner applicationRunner(UserRepo userRepo, RoleRepo roleRepo, UserRoleRepo userRoleRepo) {
        return args -> {
            Role adminRole = roleRepo.findByName(com.nkh.ecommercebackend.common.Role.ADMIN.name())
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName(com.nkh.ecommercebackend.common.Role.ADMIN.name());
                        return roleRepo.save(role);
                    });
            Optional<User> userOptional = userRepo.findByUsername("admin");
            if (userOptional.isEmpty()) {
                User user = new User();
                user.setFullName("Admin Hieu");
                user.setPhoneNumber("1234567890");
                user.setGender(Gender.MALE);
                user.setEmail("admin@example.com");
                user.setDateBirth(LocalDate.of(2003, 7, 10));
                user.setUsername("admin");
                user.setPassword(passwordEncoder.encode("admin123"));
                userRepo.save(user);
                UserRole userRole = new UserRole();
                userRole.setUser(user);
                userRole.setRole(adminRole);
                userRoleRepo.save(userRole);
                log.info("Admin Hieu has been created");
            }
        };
    }
}
