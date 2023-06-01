package io.allteran.cicerone.backbone.service;

import io.allteran.cicerone.backbone.domain.User;
import io.allteran.cicerone.backbone.dto.AuthResponse;
import io.allteran.cicerone.backbone.exception.TokenException;
import io.allteran.cicerone.backbone.security.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthService {
    @Value("${message.auth.fail}")
    private String MESSAGE_AUTH_FAILED;
    @Value("${message.auth.success}")
    private String MESSAGE_AUTH_SUCCESS;
    @Value("${message.token.valid}")
    private String MESSAGE_TOKEN_VALID;
    @Value("${message.token.invalid}")
    private String MESSAGE_TOKEN_INVALID;

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Mono<AuthResponse> login(String login, String password) {
        return userService.findByEmail(login)
                .singleOptional()
                .flatMap(userOptional -> {
                    if(userOptional.isEmpty()) {
                        return Mono.just(new AuthResponse(login, null, MESSAGE_AUTH_FAILED));
                    }
                    User user = userOptional.get();
                    if(!passwordEncoder.matches(password, user.getPassword())) {
                        return Mono.just(new AuthResponse(login, null, MESSAGE_AUTH_FAILED));
                    }
                    var token = jwtUtil.generateToken(user);
                    return Mono.just(new AuthResponse(login, token, MESSAGE_AUTH_SUCCESS));
                });
    }

    public Mono<String> validateToken(String token) {
        try{

            return jwtUtil.validateToken(token) ? Mono.just(MESSAGE_TOKEN_VALID)
                    : Mono.error(new TokenException(MESSAGE_TOKEN_INVALID));
        } catch (JwtException jwtException) {
            return Mono.error(new TokenException(MESSAGE_TOKEN_INVALID));
        }
    }

}
