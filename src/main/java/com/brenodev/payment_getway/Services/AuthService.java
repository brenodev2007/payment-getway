package com.brenodev.payment_getway.Services;

import com.brenodev.payment_getway.DTOs.AuthResponse;
import com.brenodev.payment_getway.DTOs.LoginRequest;
import com.brenodev.payment_getway.DTOs.RegisterRequest;
import com.brenodev.payment_getway.Entity.ApiUser;
import com.brenodev.payment_getway.Enums.Role;
import com.brenodev.payment_getway.Repositories.ApiUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ApiUserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new IllegalArgumentException(
                    "Username já está em uso: " + request.username()
            );
        }

        Role role = request.role() != null
                ? request.role()
                : Role.MERCHANT;

        ApiUser user = new ApiUser(
                request.username(),
                passwordEncoder.encode(request.password()),
                role
        );
        userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        String token = tokenService.generateToken(authentication);

        return new AuthResponse(
                token,
                user.getUsername(),
                user.getRole().name()
        );
    }

    public AuthResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        String token = tokenService.generateToken(authentication);

        ApiUser user = userRepository.findByUsername(request.username())
                .orElseThrow();

        return new AuthResponse(
                token,
                user.getUsername(),
                user.getRole().name()
        );
    }
}
