package com.solafides.virtual_pet_reborn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthenticationManagerConfig {

    private final CustomReactiveUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationManagerConfig(CustomReactiveUserDetailsService userDetailsService,
                                       PasswordEncoder passwordEncoder) {

        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;

    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager() {

        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;

    }

}
