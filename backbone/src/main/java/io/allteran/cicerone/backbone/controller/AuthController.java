package io.allteran.cicerone.backbone.controller;

import io.allteran.cicerone.backbone.dto.AuthRequest;
import io.allteran.cicerone.backbone.dto.AuthResponse;
import io.allteran.cicerone.backbone.service.AuthService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Mono<AuthResponse> login(@RequestBody AuthRequest request) {
        return authService.login(request.getLogin(), request.getPassword());
    }

    //in case with validation token from API more important for us is to receive bad response, so we should catch that type of cases
    @PostMapping("/validateToken")
    public Mono<String> validateToken(@Param("token") String token) {
        return authService.validateToken(token);
    }
}
