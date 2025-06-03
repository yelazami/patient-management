package com.azami.auth_service.Service;

import com.azami.auth_service.DTO.LoginRequestDTO;
import com.azami.auth_service.Model.User;
import com.azami.auth_service.Util.JwtUtil;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public Optional<String> authenticate(LoginRequestDTO loginRequestDTO) {
        return userService
                .findByEmail(loginRequestDTO.getEmail())
                .filter(user1 -> passwordEncoder.matches(loginRequestDTO.getPassword(), user1.getPassword()))
                .map(u -> jwtUtil.generateToken(u.getEmail(), u.getRole()))
        ;
    }

    public boolean validateToken(String token) {
        try {
            jwtUtil.validateToken(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
