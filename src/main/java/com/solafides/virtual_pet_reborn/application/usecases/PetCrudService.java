package com.solafides.virtual_pet_reborn.application.usecases;

import com.solafides.virtual_pet_reborn.adapter.repository.entities.PetEntity;
import com.solafides.virtual_pet_reborn.adapter.web.controllers.PetMapper;
import com.solafides.virtual_pet_reborn.application.exceptions.PetCreationException;
import com.solafides.virtual_pet_reborn.application.exceptions.PetDeletionException;
import com.solafides.virtual_pet_reborn.application.exceptions.PetReadingException;
import com.solafides.virtual_pet_reborn.application.port.out.PetCrudRepository;
import com.solafides.virtual_pet_reborn.application.port.out.PetStorage;
import com.solafides.virtual_pet_reborn.domain.Pet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.error;

@Service
@RequiredArgsConstructor
public final class PetCrudService {

    private final PetCrudRepository petCrudRepository;
    private final PetStorage petStorage;

    public Mono<Pet> save(PetEntity petEntity) {

        return petCrudRepository.save(petEntity)

                // If it doesn't work, switch to error
                .switchIfEmpty(error(new PetCreationException("Repository returned empty mono.")))

                // Mapping the entity
                .map(entity -> new Pet(
                        entity.getId(),
                        entity.getMasterId(),
                        entity.getName(),
                        entity.getPetType(),
                        entity.getEnergy(),
                        entity.getMood(),
                        entity.getPetLevel(),
                        entity.getLastFeedTime()
                ))

                // Side effects
                .doOnSuccess(pet -> petStorage.add(petEntity.getMasterId(), pet))

                // Error handling
                .onErrorMap(PetCreationException::new);

    }

    public Mono<Pet> findById(Long petId) {

        return petCrudRepository.findById(petId)

                // If it doesn't work, switch to error
                .switchIfEmpty(error(new PetReadingException("Repository returned empty mono.")))

                // Mapping the entity
                .map(entity -> new Pet(
                        entity.getId(),
                        entity.getMasterId(),
                        entity.getName(),
                        entity.getPetType(),
                        entity.getEnergy(),
                        entity.getMood(),
                        entity.getPetLevel(),
                        entity.getLastFeedTime()
                ))

                // Side effects: Add found pet to Storage
                .doOnSuccess(pet -> petStorage.add(pet.getMasterId(), pet))

                // Error handling
                .onErrorMap(PetReadingException::new);

    }

    public Flux<Pet> findAll() {
        return petCrudRepository.findAll().map(petEntity -> new Pet(
                petEntity.getId(),
                petEntity.getMasterId(),
                petEntity.getName(),
                petEntity.getPetType(),
                petEntity.getEnergy(),
                petEntity.getMood(),
                petEntity.getPetLevel(),
                petEntity.getLastFeedTime()
        ));
    }

    public Mono<Void> deleteById(Long petId) {

        return petCrudRepository.deleteById(petId)

                // TODO since there is no mono returned by the repository we cannot directly remove from the storage

                // Error handling
                .onErrorMap(PetDeletionException::new);

    }

    public Flux<Pet> findAllByMasterId(Long id) {

        return petCrudRepository.findAllByMasterId(id)

                .switchIfEmpty(error(new PetReadingException("No pets found for user with id " + id)))

                // Anti corruption layer
                .map(petEntity -> new Pet(
                        petEntity.getId(),
                        petEntity.getMasterId(),
                        petEntity.getName(),
                        petEntity.getPetType(),
                        petEntity.getEnergy(),
                        petEntity.getMood(),
                        petEntity.getPetLevel(),
                        petEntity.getLastFeedTime()
                ))

                // Side effects: For every found pet we add it to the user's storage to keep it in memory.
                .doOnNext(pet -> petStorage.add(pet.getMasterId(), pet))

                // Map generic errors
                .onErrorMap(PetReadingException::new);

    }

}
