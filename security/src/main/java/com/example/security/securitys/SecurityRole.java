package com.example.security.securitys;



import lombok.RequiredArgsConstructor;
import com.example.core.entity.Role;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public class SecurityRole implements GrantedAuthority {

    private final Role role;

    @Override
    public String getAuthority() {
        return role.getName();
    }
}
