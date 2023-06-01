package io.allteran.cicerone.backbone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String login;
    private String token;
    private String message;
}
