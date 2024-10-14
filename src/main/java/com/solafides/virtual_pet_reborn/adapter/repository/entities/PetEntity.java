package com.solafides.virtual_pet_reborn.adapter.repository.entities;

import com.solafides.virtual_pet_reborn.domain.EnergyLevel;
import com.solafides.virtual_pet_reborn.domain.Mood;
import com.solafides.virtual_pet_reborn.domain.PetLevel;
import com.solafides.virtual_pet_reborn.domain.PetType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "pets")
public final class PetEntity {

    @Id
    private final Long id;

    @Column("master_id")
    private final Long masterId;

    private final String name;

    @Column("pet_type")
    private final PetType petType;
    private final EnergyLevel energy;
    private final Mood mood;

    @Column("pet_level")
    private final PetLevel petLevel;

    @Column("last_feed_time")
    private final long lastFeedTime;

}
