package com.example.security.service.impl;

import com.example.security.repository.UserRepositorySecurity;
import com.example.security.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import com.example.core.entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepositorySecurity userRepositorySecurity;
    @Override
    public Optional<User> getByLogin(@NonNull String login) {
        return userRepositorySecurity.findUserByLogin(login);
    }
}
