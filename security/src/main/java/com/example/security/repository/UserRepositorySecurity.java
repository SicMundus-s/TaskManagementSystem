package com.example.security.repository;


import com.example.core.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositorySecurity extends JpaRepository<User, Long> {

    Optional<User> findUserByLogin(String login);
}