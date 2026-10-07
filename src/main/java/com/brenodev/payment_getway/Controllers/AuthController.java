package com.brenodev.payment_getway.Controllers;

import com.brenodev.payment_getway.DTOs.AuthResponse;
import com.brenodev.payment_getway.DTOs.LoginRequest;
import com.brenodev.payment_getway.DTOs.RegisterRequest;
import com.brenodev.payment_getway.Services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        // Com JWT stateless, o logout é responsabilidade do client
        // (descartar o token). Aqui retornamos confirmação.
        return ResponseEntity.ok(
                Map.of("message", "Logout realizado com sucesso")
        );
    }
}
