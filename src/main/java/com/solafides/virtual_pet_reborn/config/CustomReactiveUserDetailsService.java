package com.solafides.virtual_pet_reborn.config;

import com.solafides.virtual_pet_reborn.application.usecases.UserCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomReactiveUserDetailsService implements ReactiveUserDetailsService {

    private final UserCrudService userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> User.builder()
                        .username(user.username())
                        .password(user.password())
                        .roles(user.role())
                        .build());

    }



}
