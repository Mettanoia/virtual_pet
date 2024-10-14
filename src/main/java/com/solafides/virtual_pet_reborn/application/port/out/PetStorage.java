package com.solafides.virtual_pet_reborn.application.port.out;

import com.solafides.virtual_pet_reborn.domain.Pet;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public final class PetStorage {

    private final Map<Long, List<Pet>> userPetsMap = new HashMap<>();

    public void add(Long userId, Pet pet) {
        if (userId == null || pet == null)
            throw new IllegalArgumentException("User ID and pet must be non-null");


        userPetsMap.computeIfAbsent(userId, k -> new ArrayList<>()).add(pet);

    }

    public List<Pet> get(Long userId) {
        return userPetsMap.getOrDefault(userId, Collections.emptyList());
    }

    public void remove(Long userId, Long petId) {

        List<Pet> pets = userPetsMap.get(userId);

        if (pets != null)
            pets.removeIf(pet -> pet.getId().equals(petId));

    }

}
