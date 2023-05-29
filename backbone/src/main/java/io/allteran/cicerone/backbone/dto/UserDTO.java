package io.allteran.cicerone.backbone.dto;

import io.allteran.cicerone.backbone.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private char[] password;
    private char[] passwordConfirm;
    private String newPassword;
    private Set<String> roles;
    private boolean isActive;
    private LocalDateTime creationDate;
}
