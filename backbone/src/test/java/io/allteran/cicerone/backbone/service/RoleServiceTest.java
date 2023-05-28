package io.allteran.cicerone.backbone.service;

import io.allteran.cicerone.backbone.domain.Role;
import io.allteran.cicerone.backbone.exception.DuplicateException;
import io.allteran.cicerone.backbone.exception.NotFoundException;
import io.allteran.cicerone.backbone.repo.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
    @Mock
    private RoleRepository repository;
    @InjectMocks
    private RoleService roleService;

    @Test
    @DisplayName("Unit Testing: RoleService.findAll. Should return all records")
    void findAll_shouldFindAll() {
        //given
        Flux<Role> roleFlux = initBaseRoles();
        final int baseRoleListSize = 5;

        //when
        Mockito.when(repository.findAll()).thenReturn(roleFlux);
        Flux<Role> findAllRoles = roleService.findAll();

        //then
        Mockito.verify(repository, atMostOnce()).findAll();
        StepVerifier
                .create(findAllRoles)
                .recordWith(ArrayList::new)
                .thenConsumeWhile(role -> role.getName().startsWith("Test"))
                .expectRecordedMatches(roles -> roles.size() == baseRoleListSize)
                //since we have reactive entities like Mono and Flux we should check the number of invocations only after subscription
                .then(() -> {
                    Mockito.verify(repository, times(1)).findAll();
                })
                .verifyComplete();
    }

    @DisplayName("Unit Testing: RoleService.findAll. Should return empty Mono<Role>. For cases when there no any role in database")
    @Test
    void findAll_shouldReturnEmptyMono() {
        //given
        Flux<Role> expectedResult = Flux.empty();

        //when
        Mockito.when(repository.findAll()).thenReturn(expectedResult);
        Flux<Role> actualRoleFlux = roleService.findAll();

        //then
        StepVerifier
                .create(actualRoleFlux)
                .recordWith(ArrayList::new)
                .expectRecordedMatches(roles -> roles.size() == 0)
                .then(() -> {
                    Mockito.verify(repository, times(1)).findAll();
                })
                .verifyComplete();
    }

    @DisplayName("Unit Testing: RoleService.findById. Should return actual role by ID")
    @Test
    void findById_shouldFindById() {
        String idToFind = "t1";
        //given
        Mono<Role> roleMono = Mono.just(new Role("t1", "TestRole"));

        //when
        Mockito.when(repository.findById(idToFind)).thenReturn(roleMono);
        Mono<Role> findById = roleService.findById(idToFind);

        //then
        StepVerifier
                .create(findById)
                .expectNextMatches(role -> role.getId().equals(idToFind))
                .then(() -> {
                    Mockito.verify(repository, Mockito.times(1)).findById(idToFind);
                })
                .verifyComplete();
    }

    @DisplayName("Unit Testing: RoleService.findById. Should return empty mono")
    @Test
    void findById_shouldReturnEmptyMono() {
        //given
        String failId = "failId";

        //when
        Mockito.when(repository.findById(failId)).thenReturn(Mono.empty());
        Mono<Role> findByIdFailed = roleService.findById(failId);

        //then
        StepVerifier
                .create(findByIdFailed)
                .expectNextCount(0)
                .then(() -> {
                    Mockito.verify(repository, times(1)).findById(failId);
                })
                .verifyComplete();
    }

    @DisplayName("Unit Testing: RoleService.findByName. Should Role by given name")
    @Test
    void findByName_shouldFindByName() {
        //given
        String nameToFind = "TestRole";
        Mono<Role> expectedRoleMono = Mono.just(new Role("test_id", nameToFind));

        //when
        Mockito.when(repository.findByName(nameToFind)).thenReturn(expectedRoleMono);
        Mono<Role> actualRoleMono = roleService.findByName(nameToFind);

        //then
        StepVerifier
                .create(actualRoleMono)
                .expectNextCount(1)
                .thenConsumeWhile(role -> role.getName().equals(nameToFind))
                .then(() -> {
                    Mockito.verify(repository, times(1)).findByName(nameToFind);
                })
                .verifyComplete();
    }

    @DisplayName("Unit Testing: RoleService.findByName. Should return empty Mono<Role>")
    @Test
    void findByName_shouldReturnEmptyMono() {
        //given
        String nameToFind = "TestName";

        //when
        Mockito.when(repository.findByName(nameToFind)).thenReturn(Mono.empty());
        Mono<Role> actualResult = roleService.findByName(nameToFind);

        //then
        StepVerifier
                .create(actualResult)
                .expectNextCount(0)
                .then(() -> {
                    Mockito.verify(repository, times(1)).findByName(nameToFind);
                })
                .verifyComplete();
    }

    @DisplayName("Unit Testing RoleService.create. Should create Role and return Mono<Role>")
    @Test
    void create_shouldCreateAndReturn() {
        //given

        Role expectedRole = new Role("test_id", "TestRoleCreated");
        Mono<Role> expectedMono = Mono.just(expectedRole);

        //when
        Mockito.when(repository.save(expectedRole)).thenReturn(expectedMono);
        Mockito.when(repository.findByName(expectedRole.getName().toLowerCase())).thenReturn(Mono.empty());
        Mono<Role> actualResult = roleService.create(expectedMono);

        //then
        StepVerifier
                .create(actualResult)
                .expectNextCount(1)
                .thenConsumeWhile(role -> role.getName().equals(expectedRole.getName()) && role.getId().equals(expectedRole.getId()))
                .then(() -> {
                    Mockito.verify(repository, times(1)).save(expectedRole);
                    Mockito.verify(repository, times(1)).findByName(expectedRole.getName().toLowerCase());
                })
                .verifyComplete();
    }

    @DisplayName("Unit Testing RoleService.create. Should throw DuplicateException")
    @Test
    void create_shouldThrowException() {
        //given
        Role givenRole = new Role("newId", "RoleWithDuplicateName");
        Mono<Role> givenRoleMono = Mono.just(givenRole);

        //when
        Mockito.when(repository.findByName(givenRole.getName().toLowerCase())).thenReturn(givenRoleMono);
        Mono<Role> actualResult = roleService.create(givenRoleMono);

        //then
        StepVerifier
                .create(actualResult)
                .then(() -> {
                    Mockito.verify(repository, Mockito.never()).save(givenRole);
                    Mockito.verify(repository, Mockito.times(1)).findByName(givenRole.getName().toLowerCase());
                })
                .expectError(DuplicateException.class)
                .verify();

    }

    @DisplayName("Unit Testing RoleService.update. Should update and return updated Mono<Role>")
    @Test
    void update_shouldReturnUpdated() {
        //given
        String existingId = "existing_id";
        Role givenRole = new Role(existingId, "Given_Role");
        Mono<Role> givenRoleMono = Mono.just(givenRole);

        Role givenUpdatedRole = new Role(existingId, "Given_Updated_Role");
        Mono<Role> givenUpdatedRoleMono = Mono.just(givenUpdatedRole);

        //when
        Mockito.when(repository.save(givenUpdatedRole)).thenReturn(givenUpdatedRoleMono);
        Mockito.when(repository.findById(givenRole.getId())).thenReturn(givenRoleMono);
        Mono<Role> actualResult = roleService.update(givenRole.getId(), givenUpdatedRoleMono);

        //then
        StepVerifier
                .create(actualResult)
                .expectNextCount(1)
                .thenConsumeWhile(role -> role.getId().equals(givenRole.getId()) && role.getName().equals(givenUpdatedRole.getName()))
                .then(() -> {
                    Mockito.verify(repository, Mockito.times(1)).findById(givenRole.getId());
                    Mockito.verify(repository, Mockito.times(1)).save(givenUpdatedRole);
                })
                .verifyComplete();
    }

    @DisplayName("Unit Testing: RoleService.create. Should throw NotFoundException")
    @Test
    void update_shouldThrowException() {
        //given
        String nonExistingId = "non_existing_id";

        Role updatedRole = new Role(null, "UpdatedRole");
        Mono<Role> updatedRoleMono = Mono.just(updatedRole);

        //when
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Mono.empty());
        Mono<Role> actualResult = roleService.update(nonExistingId, updatedRoleMono);

        //then
        StepVerifier
                .create(actualResult)
                .then(() -> {
                    Mockito.verify(repository, Mockito.never()).save(updatedRole);
                    Mockito.verify(repository, Mockito.times(1)).findById(nonExistingId);
                })
                .expectError(NotFoundException.class)
                .verify();
    }

    @DisplayName("Unit Testing: RoleService.delete. Should delete and return Mono<Void>")
    @Test
    void delete_shouldDelete() {
        //given
        String idToDelete = "idToDelete";
        Mono<Role> roleToDeleteMono = Mono.just(new Role(idToDelete, "RoleToDelete"));

        //when
        Mockito.when(repository.deleteById(idToDelete)).thenReturn(Mono.empty());
        Mockito.when(repository.findById(idToDelete)).thenReturn(roleToDeleteMono);
        Mono<Void> actualResult = roleService.delete(idToDelete);

        //then
        StepVerifier
                .create(actualResult)
                .then(() -> {
                    Mockito.verify(repository, Mockito.times(1)).findById(idToDelete);
                    Mockito.verify(repository, Mockito.times(1)).deleteById(idToDelete);

                })
                .verifyComplete();
    }

    @DisplayName("Unit Testing: RoleService.delete. Should throw NotFoundException")
    @Test
    void delete_shouldThrowExceptionAndNotDelete() {
        //given
        String nonExistingId = "nonExistingId";

        //when
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Mono.empty());
        Mono<Void> actualResult = roleService.delete(nonExistingId);

        //then
        StepVerifier
                .create(actualResult)
                .then(() -> {
                    Mockito.verify(repository, Mockito.times(1)).findById(nonExistingId);
                    Mockito.verify(repository, Mockito.never()).deleteById(nonExistingId);
                })
                .expectError(NotFoundException.class)
                .verify();
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