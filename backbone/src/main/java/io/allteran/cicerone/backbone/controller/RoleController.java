package io.allteran.cicerone.backbone.controller;

import io.allteran.cicerone.backbone.dto.RoleDTO;
import io.allteran.cicerone.backbone.service.RoleService;
import io.allteran.cicerone.backbone.util.EntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/backbone/roles")
public class RoleController {
    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }
    @GetMapping("/")
    public Flux<RoleDTO> findAll() {
        return roleService.findAll().map(EntityConverter::convertToDTO);
    }

    @GetMapping("/get/{id}")
    public Mono<RoleDTO> findById(@PathVariable("id") String id) {
        return roleService.findById(id).map(EntityConverter::convertToDTO);
    }

    @GetMapping("/search/name/{name}")
    public Mono<RoleDTO> searchByName(@PathVariable("name") String name) {
        return roleService.findByName(name)
                .map(EntityConverter::convertToDTO);
    }

    @PostMapping("/new")
    public Mono<RoleDTO> create(@RequestBody RoleDTO body) {
        return roleService.create(Mono.just(EntityConverter.convertToEntity(body)))
                .map(EntityConverter::convertToDTO);
    }

    @PutMapping("/update/{id}")
    public Mono<RoleDTO> update(@PathVariable("id") String idFromDb,
                                @RequestBody RoleDTO body) {
        return roleService.update(
                        idFromDb,
                        Mono.just(EntityConverter.convertToEntity(body)))
                .map(EntityConverter::convertToDTO);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<Void> delete(@PathVariable("id") String id) {
        return roleService.delete(id);
    }

}
