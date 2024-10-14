package com.solafides.virtual_pet_reborn.config;

import com.solafides.virtual_pet_reborn.adapter.repository.entities.UserEntity;
import com.solafides.virtual_pet_reborn.application.port.out.UserCrudRepository;
import com.solafides.virtual_pet_reborn.domain.User;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Profile;
import reactor.core.publisher.Flux;

import static java.util.List.of;

@Configuration
@Profile("runner")  // This ensures it runs only under the 'runner' profile
public class DataInitializer {

    @Bean
    ApplicationRunner initUsers(UserCrudRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> userRepository.saveAll(Flux.fromIterable(of(
                new UserEntity(null, "Miguel", passwordEncoder.encode("adminpassword"), "ADMIN"),
                new UserEntity(null, "Pablo", passwordEncoder.encode("user1password"), "USER"),
                new UserEntity(null, "Juan", passwordEncoder.encode("user2password"), "USER")
        ))).subscribe();
    }

}
