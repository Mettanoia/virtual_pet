package com.solafides.virtual_pet_reborn.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum Food {

    APPLE(1, Set.of(FoodType.CARBOHYDRATE)),
    STEAK(3, Set.of(FoodType.PROTEIN, FoodType.FAT)),
    BREAD(2, Set.of(FoodType.CARBOHYDRATE)),
    SALMON(4, Set.of(FoodType.PROTEIN, FoodType.FAT)),
    PASTA(3, Set.of(FoodType.CARBOHYDRATE)),
    AVOCADO(2, Set.of(FoodType.FAT, FoodType.CARBOHYDRATE)),
    EGGS(3, Set.of(FoodType.PROTEIN, FoodType.FAT)),
    MIXED_NUTS(5, Set.of(FoodType.PROTEIN, FoodType.FAT, FoodType.CARBOHYDRATE));

    private final int energyValue;
    private final Set<FoodType> macronutrients;

}