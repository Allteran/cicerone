package io.allteran.cicerone.backbone.repo;

import io.allteran.cicerone.backbone.domain.Role;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface RoleRepository extends ReactiveMongoRepository<Role, String> {
    @Query("{'name' :  ?0}")
    Mono<Role> findByName(String name);
}
