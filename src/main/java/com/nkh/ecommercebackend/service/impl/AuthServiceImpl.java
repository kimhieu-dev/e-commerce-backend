package com.nkh.ecommercebackend.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nkh.ecommercebackend.dto.request.*;
import com.nkh.ecommercebackend.dto.response.IntrospectRes;
import com.nkh.ecommercebackend.dto.response.LoginRes;
import com.nkh.ecommercebackend.entity.InvalidatedToken;
import com.nkh.ecommercebackend.entity.User;
import com.nkh.ecommercebackend.exception.BusinessException;
import com.nkh.ecommercebackend.exception.ErrorCode;
import com.nkh.ecommercebackend.repository.InvalidatedTokenRepo;
import com.nkh.ecommercebackend.service.AuthService;
import com.nkh.ecommercebackend.service.UserRoleService;
import com.nkh.ecommercebackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRoleService userRoleService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final InvalidatedTokenRepo invalidatedTokenRepo;

    @Value("${jwt.secret}")
    protected String SECRET;

    @Value("${jwt.access-token-expiration}")
    protected long ACCESS_TOKEN_EXPIRATION;

    @Value("${jwt.refresh-token-expiration}")
    protected long REFRESH_TOKEN_EXPIRATION;

    @Value("${jwt.issuer}")
    protected String ISSUER;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterReq userReq) {
        userRoleService.createUser(userReq);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginRes login(LoginReq request) {
        User user = userService.checkIfUsernameExists(request.getUsername())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new BusinessException(ErrorCode.UNAUTHENTICATED);
        }

        String token = generateToken(user);
        return LoginRes.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IntrospectRes introspect(IntrospectReq request) {
        try {
            verifyToken(request.getToken(), false);
            return IntrospectRes.builder().valid(true).build();
        } catch (Exception e) {
            log.error("Introspect Ex: ", e);
            return IntrospectRes.builder().valid(false).build();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logout(LogoutReq request) {
        try {
            SignedJWT signedJWT = verifyToken(request.getToken(), true);
            String jit = signedJWT.getJWTClaimsSet().getJWTID();
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jit)
                    .expiryTime(expiryTime)
                    .build();

            invalidatedTokenRepo.save(invalidatedToken);
        } catch (JOSEException | ParseException e) {
            log.error("Error during logout: {}", e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginRes refreshToken(RefreshTokenReq request) {
        try {
            SignedJWT signedJWT = verifyToken(request.getRefreshToken(), true);
            String jit = signedJWT.getJWTClaimsSet().getJWTID();
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            // Invalidate current refresh token
            invalidatedTokenRepo.save(InvalidatedToken.builder()
                    .id(jit)
                    .expiryTime(expiryTime)
                    .build());

            String username = signedJWT.getJWTClaimsSet().getSubject();
            User user = userService.checkIfUsernameExists(username)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

            String token = generateToken(user);
            return LoginRes.builder()
                    .token(token)
                    .authenticated(true)
                    .build();
        } catch (JOSEException | ParseException e) {
            throw new BusinessException(ErrorCode.UNAUTHENTICATED);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void forgotPassword(ForgotPasswordReq request) {
        // Implementation for forgot password (send email with reset link/code)
//        userService.findByEmail(request.getEmail())
//                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        log.info("Forgot password requested for email: {}", request.getEmail());
        // TODO: Integrate with an email service
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SECRET.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh)
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(REFRESH_TOKEN_EXPIRATION, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date()))) {
            throw new BusinessException(ErrorCode.UNAUTHENTICATED);
        }

        if (invalidatedTokenRepo.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new BusinessException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer(ISSUER)
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(ACCESS_TOKEN_EXPIRATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("userId", user.getId())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SECRET.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getUserRoles())) {
            user.getUserRoles().forEach(userRole -> {
                stringJoiner.add("ROLE_" + userRole.getRole().getName());
            });
        }
        return stringJoiner.toString();
    }
}
