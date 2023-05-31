package io.allteran.cicerone.backbone.controller;

import io.allteran.cicerone.backbone.dto.UserDTO;
import io.allteran.cicerone.backbone.service.UserService;
import io.allteran.cicerone.backbone.util.EntityConverter;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/backbone/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/", ""})
    public Flux<UserDTO> findAll() {
        return userService.findAll().map(EntityConverter::convertToDTO);
    }

    @GetMapping("/get/{id}")
    public Mono<UserDTO> findById(@PathVariable("id") String id) {
        return userService.findById(id).map(EntityConverter::convertToDTO);
    }

    @PostMapping("/new")
    public Mono<UserDTO> create(@RequestBody UserDTO userDTO) {
        return userService.create(Mono.just(EntityConverter.convertToEntity(userDTO)))
                .map(EntityConverter::convertToDTO);
    }

    @PutMapping("/update/{id}")
    public Mono<UserDTO> update(@PathVariable("id") String idFromDb,
                                @RequestBody UserDTO body) {
        return userService.update(idFromDb, Mono.just(EntityConverter.convertToEntity(body)))
                .map(EntityConverter::convertToDTO);
    }
}
