package io.allteran.cicerone.backbone.service;

import io.allteran.cicerone.backbone.domain.Role;
import io.allteran.cicerone.backbone.exception.DuplicateException;
import io.allteran.cicerone.backbone.exception.NotFoundException;
import io.allteran.cicerone.backbone.repo.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
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
        return roleMono.flatMap(repository::save);

    }

    @Transactional
    public Mono<Role> update(String idFromDb, Mono<Role> roleMono) {
        return roleMono.flatMap(role -> repository.findById(idFromDb)
                .flatMap(roleFromDb ->{
                    if(roleFromDb == null) {
                        return Mono.error(new NotFoundException("Role update error: can't find roleMono with given ID [" + idFromDb + "]"));
                    }
                    roleFromDb.setName(role.getName());
                    return repository.save(role);
                }));
    }

    @Transactional
    public Mono<Void> delete(String id) {
        return repository.findById(id)
                .flatMap(roleFromDb -> (roleFromDb == null)
                        ? Mono.error(new NotFoundException("Role delete error: can't find role with given ID [" + id + "]"))
                        : repository.deleteById(id));
    }
}
