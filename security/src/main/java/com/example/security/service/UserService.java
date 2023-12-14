package com.example.security.service;


import lombok.NonNull;
import com.example.core.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getByLogin(@NonNull String login);
}
