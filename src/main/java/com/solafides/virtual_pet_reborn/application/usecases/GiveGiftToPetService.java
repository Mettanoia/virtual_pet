package com.solafides.virtual_pet_reborn.application.usecases;

import com.solafides.virtual_pet_reborn.adapter.repository.entities.PetEntity;
import com.solafides.virtual_pet_reborn.domain.EnergyLevel;
import com.solafides.virtual_pet_reborn.domain.Gift;
import com.solafides.virtual_pet_reborn.domain.Mood;
import com.solafides.virtual_pet_reborn.domain.Pet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.just;

@Service
@RequiredArgsConstructor
public final class GiveGiftToPetService {

    private final PetCrudService petCrudService;

    public Mono<Mood> giveGiftToPet(Pet pet, Gift gift) {

        Mood mood = pet.gift(gift);

        return petCrudService.save(new PetEntity(
                        pet.getId(),
                        pet.getMasterId(),
                        pet.getName(),
                        pet.getPetType(),
                        pet.getEnergy(),
                        pet.getMood(),
                        pet.getPetLevel(),
                        pet.getLastFeedTime()
                ))

                .then(just(mood));

    }

}
