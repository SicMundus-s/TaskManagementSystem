package com.example.security.service;

import com.example.core.entity.User;
import com.example.security.repository.UserRepositorySecurity;
import com.example.security.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepositorySecurity userRepositorySecurity;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testGetByLogin_WhenUserExists() {
        String login = "john_doe@mail.ru";
        User user = new User();

        BDDMockito.given(userRepositorySecurity.findUserByLogin(login))
                .willReturn(Optional.of(user));

        Optional<User> result = userService.getByLogin(login);
        assertEquals(Optional.of(user), result);
    }

    @Test
    public void testGetByLogin_WhenUserDoesNotExist() {
        String login = "non_existing_user";

        BDDMockito.given(userRepositorySecurity.findUserByLogin(login))
                .willReturn(Optional.empty());

        Optional<User> result = userService.getByLogin(login);
        assertEquals(Optional.empty(), result);
    }

}
