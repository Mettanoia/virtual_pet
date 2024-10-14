package com.solafides.virtual_pet_reborn.adapter.web.dtos;

import com.solafides.virtual_pet_reborn.domain.EnergyLevel;
import com.solafides.virtual_pet_reborn.domain.Mood;
import com.solafides.virtual_pet_reborn.domain.PetLevel;
import com.solafides.virtual_pet_reborn.domain.PetType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class PetDto {

    private Long id;
    private Long masterId;
    private String name;
    private PetType petType;
    private EnergyLevel energy;
    private Mood mood;
    private PetLevel petLevel;
    private long lastFeedTime;

}
