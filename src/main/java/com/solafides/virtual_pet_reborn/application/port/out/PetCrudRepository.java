package com.solafides.virtual_pet_reborn.application.port.out;

import com.solafides.virtual_pet_reborn.adapter.repository.entities.PetEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PetCrudRepository extends ReactiveCrudRepository<PetEntity, Long> {

    Flux<PetEntity> findAllByMasterId(Long masterId);

}
