package com.azami.auth_service.Controller;

import com.azami.auth_service.DTO.LoginRequestDTO;
import com.azami.auth_service.DTO.LoginResponseDTO;
import com.azami.auth_service.Service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Generate token on user login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginRequestDTO loginRequestDTO
    ) {
        Optional<String> tokenOptional = authService.authenticate(loginRequestDTO);

        return tokenOptional.map(token -> ResponseEntity.ok(
                new LoginResponseDTO(
                        token
                )
        )).orElseGet(
                () -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        );

    }

    @Operation(summary = "Validate Token")
    @GetMapping("/validate")
    public ResponseEntity<Void> validateToken(
            @RequestHeader("Authorization") String authHeader // Authorization: Bearer <token>
    ) {
        ResponseEntity<Void> unauthorizedResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorizedResponse;
        }

        return authService.validateToken(authHeader.substring(7))
                ? ResponseEntity.ok().build()
                : unauthorizedResponse;
    }
}
