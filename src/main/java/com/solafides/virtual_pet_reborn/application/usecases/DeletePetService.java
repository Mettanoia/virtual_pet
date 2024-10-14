package com.solafides.virtual_pet_reborn.application.usecases;

import com.solafides.virtual_pet_reborn.application.exceptions.PetDeletionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.just;

@Service
@RequiredArgsConstructor
public final class DeletePetService {

    private final PetCrudService petCrudService;

    public Mono<Void> deletePet(Long id) {

        return petCrudService.deleteById(id)

                .onErrorMap(PetDeletionException::new);

    }

}
