package io.allteran.cicerone.backbone.service;

import io.allteran.cicerone.backbone.domain.Role;
import io.allteran.cicerone.backbone.repo.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
    @Mock
    private RoleRepository repository;
    @InjectMocks
    private RoleService roleService;
    private final int BASE_ROLE_LIST_SIZE = 5;

    @Test
    @DisplayName("Testing finsAll. Should return all records")
    void findAll_shouldFindAll() {
        //given
        Flux<Role> roleFlux = initBaseRoles();

        //when
        Mockito.when(repository.findAll()).thenReturn(roleFlux);
        Flux<Role> findAllRoles = roleService.findAll();

        //then
        StepVerifier
                .create(findAllRoles)
                .recordWith(ArrayList::new)
                .thenConsumeWhile(role -> role.getName().startsWith("Test"))
                .expectRecordedMatches(roles -> roles.size() == BASE_ROLE_LIST_SIZE)
                .verifyComplete();
    }

    @DisplayName("Testing findById. Should return actual role by ID")
    @Test
    void findById_shouldFindById() {
        String idToFind = "t1";
        //given
        Mono<Role> roleMono = Mono.just(new Role("t1", "TestRole"));

        //when
        Mockito.when(repository.findById(idToFind)).thenReturn(roleMono);
        Mono<Role> findById = roleService.findById(idToFind);

        //then
        StepVerifier.
                create(findById)
                .expectNextMatches(role -> role.getId().equals(idToFind))
                .verifyComplete();
    }

    @DisplayName("Testing findById. Should return empty mono")
    @Test
    void findById_shouldReturnEmptyMono() {
        //given
        String failId = "failId";

        //when
        Mockito.when(repository.findById(failId)).thenReturn(Mono.empty());
        Mono<Role> findById = roleService.findById(failId);

        //then
        //TODO: finish fail test case
    }

    private Flux<Role> initBaseRoles() {
        return Flux.just(
                new Role("t1", "TestAdmin"),
                new Role("t2", "TestSuperAdmin"),
                new Role("t3", "TestUser"),
                new Role("t4", "TestManager"),
                new Role("t5", "TestAnonymous"));
    }
}