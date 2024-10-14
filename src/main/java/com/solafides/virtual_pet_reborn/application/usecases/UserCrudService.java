package com.solafides.virtual_pet_reborn.application.usecases;

import com.solafides.virtual_pet_reborn.application.exceptions.UserReadingException;
import com.solafides.virtual_pet_reborn.application.port.out.UserCrudRepository;
import com.solafides.virtual_pet_reborn.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.error;

@Service
@RequiredArgsConstructor
public final class UserCrudService {

    private final UserCrudRepository userCrudRepository;

    public Mono<User> findByUsername(String username) {

        // Fetch user using user name
        return userCrudRepository.findByUsername(username)

                // Map in case of error
                .onErrorMap(UserReadingException::new)

                // If user is not found switch to error reaction chain
                .switchIfEmpty(error(new UserReadingException("User with username: " + username + " not found.")))

                // Anti corruption layer
                .map(userEntity -> new User(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword(), userEntity.getRole()));

    }

}
