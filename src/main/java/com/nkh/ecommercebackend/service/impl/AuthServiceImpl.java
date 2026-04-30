package com.nkh.ecommercebackend.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nkh.ecommercebackend.dto.request.LoginReq;
import com.nkh.ecommercebackend.dto.request.RegisterUserReq;
import com.nkh.ecommercebackend.dto.response.LoginRes;
import com.nkh.ecommercebackend.entity.User;
import com.nkh.ecommercebackend.exception.BusinessException;
import com.nkh.ecommercebackend.exception.ErrorCode;
import com.nkh.ecommercebackend.service.AuthService;
import com.nkh.ecommercebackend.service.UserRoleService;
import com.nkh.ecommercebackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRoleService userRoleService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    protected String SIGNER_KEY;

    @Override
    public void register(RegisterUserReq userReq) {
        userRoleService.createUser(userReq);
    }

    @Override
    public LoginRes login(LoginReq request) {
        Optional<User> userOptional = userService.checkIfUsernameExists(request.getUsername());
        if (userOptional.isEmpty()) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        Boolean authenticated = passwordEncoder.matches(request.getPassword(), userOptional.get().getPassword());
        if (!authenticated) {
            throw new BusinessException(ErrorCode.UNAUTHENTICATED);
        }

        String token = generateToken(request.getUsername());

        return LoginRes.builder()
                .token(token)
                .authenticated(authenticated)
                .build();
    }

    private String generateToken(String username) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("kimhieu-dev.com")
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + 3600 * 1000))
                .claim("customClaim", "Claim")
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);


        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Error creating token: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
