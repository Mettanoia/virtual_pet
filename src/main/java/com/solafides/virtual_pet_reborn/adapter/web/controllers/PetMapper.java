package com.solafides.virtual_pet_reborn.adapter.web.controllers;

import com.solafides.virtual_pet_reborn.adapter.repository.entities.PetEntity;
import com.solafides.virtual_pet_reborn.adapter.web.dtos.PetDto;

public class PetMapper {

    public static PetEntity dtoToEntity(PetDto petDto) {
        if (petDto == null)
            throw new IllegalArgumentException("Pet Dto cannot be null.");

        return new PetEntity(
                petDto.getId(),
                petDto.getMasterId(),
                petDto.getName(),
                petDto.getPetType(),
                petDto.getEnergy(),
                petDto.getMood(),
                petDto.getPetLevel(),
                petDto.getLastFeedTime()
        );

    }

}
