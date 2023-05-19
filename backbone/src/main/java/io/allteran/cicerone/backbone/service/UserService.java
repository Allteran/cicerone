package io.allteran.cicerone.backbone.service;

import io.allteran.cicerone.backbone.repo.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }
}
