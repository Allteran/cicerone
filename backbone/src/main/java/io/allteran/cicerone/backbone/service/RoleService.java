package io.allteran.cicerone.backbone.service;

import io.allteran.cicerone.backbone.domain.Role;
import io.allteran.cicerone.backbone.exception.DuplicateException;
import io.allteran.cicerone.backbone.exception.NotFoundException;
import io.allteran.cicerone.backbone.repo.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class RoleService {
    private final RoleRepository repository;

    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    public Flux<Role> findAll() {
        return repository.findAll();
    }

    public Mono<Role> findById(String id) {
        return repository.findById(id);
    }

    public Mono<Role> findByName(String name) {
        return repository.findByName(name);
    }

    @Transactional
    public Mono<Role> create(Mono<Role> roleMono) {
        return roleMono.flatMap(role ->
                repository.existsByName(role.getName().toLowerCase())
                        .flatMap(exists -> (exists)
                                ? Mono.error(new DuplicateException("Role name should be unique"))
                                : repository.save(role)
//                repository.findByName(role.getName().toLowerCase())
//                        .singleOptional()
//                        .flatMap(existedRole -> (existedRole.isEmpty())
//                                ? repository.save(role)
//                                : Mono.error(new DuplicateException("Role name should be unique"))
//                        )

        ));

    }

    @Transactional
    public Mono<Role> update(String idFromDb, Mono<Role> roleMono) {
        return roleMono.flatMap(role ->
                repository.findById(idFromDb)
                        .switchIfEmpty(Mono.error(new NotFoundException("Role update error: can't find roleMono with given ID [" + idFromDb + "]")))
                        .flatMap(roleFromDb ->{
                            roleFromDb.setName(role.getName());
                            return repository.save(role);
                        }));
    }

    @Transactional
    public Mono<Void> delete(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Role update error: can't find roleMono with given ID [" + id + "]")))
                .flatMap(roleFromDb -> repository.deleteById(id));
    }
}
