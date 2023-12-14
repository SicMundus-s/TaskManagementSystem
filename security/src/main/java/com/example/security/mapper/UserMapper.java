package com.example.security.mapper;



import com.example.security.dto.UserDTO;
import com.example.core.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User mapToEntity(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin());
        user.setPassword(userDTO.getPassword());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        return user;
    }
}
