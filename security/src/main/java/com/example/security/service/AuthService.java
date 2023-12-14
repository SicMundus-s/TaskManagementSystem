package com.example.security.service;


import com.example.security.dto.UserDTO;
import com.example.security.entity.jwt.JwtRequest;
import com.example.security.entity.jwt.JwtResponse;

public interface AuthService {

    JwtResponse registration(UserDTO userDTO);
    JwtResponse login(JwtRequest authRequest);
    JwtResponse getAccessToken(String refreshToken);
    JwtResponse refresh(String refreshToken);
}
