package com.example.security.securitys;


import com.example.security.repository.UserRepositorySecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepositorySecurity userRepositorySecurity;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        var u = userRepositorySecurity.findUserByLogin(login);

        return u.map(SecurityUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found " + login));
    }
}
