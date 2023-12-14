package com.example.security.entity.jwt;

import com.example.security.securitys.SecurityRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import com.example.core.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

@Getter
@Setter
@RequiredArgsConstructor
public class JwtAuth implements Authentication {

    private boolean authenticated;
    private String username;
    private String firstName;
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles()
                .stream()
                .map(SecurityRole::new)
                .collect(Collectors.toList());
    }

    @Override
    public Object getCredentials() { return null; }

    @Override
    public Object getDetails() { return null; }

    @Override
    public Object getPrincipal() { return username; }

    @Override
    public boolean isAuthenticated() { return authenticated; }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() { return firstName; }

}