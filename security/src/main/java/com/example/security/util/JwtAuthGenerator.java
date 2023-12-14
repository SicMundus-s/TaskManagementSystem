package com.example.security.util;

import com.example.security.repository.UserRepositorySecurity;
import io.jsonwebtoken.Claims;
import com.example.security.entity.jwt.JwtAuth;
import lombok.RequiredArgsConstructor;
import com.example.core.entity.User;
import org.springframework.stereotype.Component;

import java.util.Optional;


@RequiredArgsConstructor
@Component
public class JwtAuthGenerator {

    private final UserRepositorySecurity userRepositorySecurity;
    public JwtAuth generate(Claims claims) {
        Optional<User> userByLogin = userRepositorySecurity.findUserByLogin(claims.getSubject());
        final JwtAuth jwtInfoToken = new JwtAuth(userByLogin.get());
        jwtInfoToken.setFirstName(claims.get("firstName", String.class));
        jwtInfoToken.setUsername(claims.getSubject());
        return jwtInfoToken;
    }
}