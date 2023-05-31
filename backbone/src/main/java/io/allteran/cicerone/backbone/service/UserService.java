package io.allteran.cicerone.backbone.service;

import io.allteran.cicerone.backbone.domain.User;
import io.allteran.cicerone.backbone.exception.DuplicateException;
import io.allteran.cicerone.backbone.exception.EntityFieldException;
import io.allteran.cicerone.backbone.exception.NotFoundException;
import io.allteran.cicerone.backbone.repo.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService implements ReactiveUserDetailsService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Mono<User> create(Mono<User> userMono) {
        return userMono.flatMap(user -> {
                    if(user.getPassword().equals(user.getPasswordConfirm())) {
                        user.setPassword(passwordEncoder.encode(user.getPassword()));
                        user.setPasswordConfirm("");
                        user.setNewPassword("");
                    } else {
                        user.setPasswordConfirm("");
                        user.setPassword("");
                        user.setNewPassword("");
                        return Mono.error(new EntityFieldException("User creation error: password don't match"));
                    }
                    if(!emailValidation(user.getEmail())) {
                        return Mono.error(new EntityFieldException("User creation error: email doesn't match email pattern"));
                    }
                    return Mono.just(user);
                })
                .flatMap(user -> repository.existsByEmail(user.getEmail())
                        .flatMap(exists -> (exists)
                                ? Mono.error(new DuplicateException("User creation error: email should be unique"))
                                : repository.save(user)));
    }

    //TODO: cover with tests this class

    public Mono<User> update(String idFromDb, Mono<User> updatedUserMono) {
        return updatedUserMono.flatMap(updatedUser -> repository.findById(idFromDb).singleOptional().flatMap(userFromDbOptional -> {
            if(userFromDbOptional.isEmpty()) {
                return Mono.error(new NotFoundException("User update error: can't find user with given ID [" + idFromDb + "]"));
            }

            User userFromDb = userFromDbOptional.get();
            String originalEmail = userFromDb.getEmail();
            if(!updatedUser.getNewPassword().equals(updatedUser.getPasswordConfirm())) {
                return Mono.error(new EntityFieldException("User update error: new password mismatch with confirmation"));
            }
            //TODO: for if you want to make admin change password without confirmation of old one - you should make separated endpoint for admin and user
            if(!passwordEncoder.matches(updatedUser.getPassword(), userFromDb.getPassword())) {
                return Mono.error(new EntityFieldException("User update error: entered current password is wrong"));
            }

            BeanUtils.copyProperties(updatedUser, userFromDb, "id", "password", "newPassword", "passwordConfirm");

            userFromDb.setPassword(passwordEncoder.encode(updatedUser.getNewPassword()));
            userFromDb.setPasswordConfirm("");
            userFromDb.setNewPassword("");
            return repository.existsByEmail(userFromDb.getEmail()).flatMap(exists -> {
                if(!originalEmail.equals(userFromDb.getEmail())) {
                    if(exists) {
                        return Mono.error(new DuplicateException("User update error: email should be unique"));
                    }
                }
                return repository.save(userFromDb);
            });
        }));
    }

    private boolean emailValidation(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }

    public Flux<User> findAll() {
        return repository.findAll();
    }

    public Mono<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Mono<User> findById(String id) {
        return repository.findById(id);
    }

    public Flux<User> findByFirstName(String firstName) {
        return repository.findAllByFirstName(firstName);
    }

    public Flux<User> findByLastName(String lastName) {
        return repository.findAllByLastName(lastName);
    }

    public Flux<User> findByRoleId(String roleId) {
        return repository.findAllByRoleId(roleId);
    }

    public Flux<User> findByRoleId(String ...roleId) {
        return repository.findAllByRoleIds(roleId);
    }

    public Flux<User> findByRoleName(String name) {
        return repository.findAllByRoleName(name);
    }

    public Flux<User> findByRoleName(String ...names) {
        return repository.findAllByRoleNames(names);
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return findByEmail(username).cast(UserDetails.class);
    }
}
