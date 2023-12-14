package com.example.security.repository;


import com.example.core.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepositorySecurity extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
