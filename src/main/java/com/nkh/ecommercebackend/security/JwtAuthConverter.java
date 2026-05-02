//package com.nkh.ecommercebackend.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
//public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {
//    @Override
//    public AbstractAuthenticationToken convert(Jwt jwt) {
//        List<String> roles = jwt.getClaimAsStringList("roles");
//
//        List<GrantedAuthority> authorities = roles.stream()
//                .map(role -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + role))
//                .toList();
//
//        return new JwtAuthenticationToken(jwt, authorities);
//    }
//
//}
