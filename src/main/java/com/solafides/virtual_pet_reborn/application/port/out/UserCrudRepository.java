package com.solafides.virtual_pet_reborn.application.port.out;

import com.solafides.virtual_pet_reborn.adapter.repository.entities.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserCrudRepository extends ReactiveCrudRepository<UserEntity, Long> {

    Mono<UserEntity> findByUsername(String username);

}
