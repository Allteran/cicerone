package io.allteran.cicerone.backbone.repo;

import io.allteran.cicerone.backbone.domain.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
    @Query("{'email': ?0}")
    Mono<User> findByEmail(String email);

    @Query("{'firstName': ?0}")
    Flux<User> findAllByFirstName(String firstName);
    @Query("{'lastName':  ?0}")
    Flux<User> findAllByLastName(String lastName);

    @Query("{'roles.id': ?0}")
    Flux<User> findAllByRoleId(String roleId);

    @Query("{'roles.id': {$in:  ?0}}")
    Flux<User> findAllByRoleIds(String ...roleId);

    @Query("{'roles.name': ?0}")
    Flux<User> findAllByRoleName(String name);

    @Query("{'roles.name': {$in: ?0}}")
    Flux<User> findAllByRoleNames(String ...names);

    Mono<Boolean> existsByEmail(String email);

    Mono<Boolean> existsById(String id);
}
