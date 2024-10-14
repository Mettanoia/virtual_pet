package com.solafides.virtual_pet_reborn.domain;

import lombok.*;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public final class Pet {


    // Constants

    private static final long REQUIRED_INTERVAL = 3600000;
    private static final Map<Environment, Map<PetType, Favorability>> FAVORABILITY_MAP = Map.of(

            Environment.DESERT,
            Map.of(PetType.FIRE, Favorability.FAVORABLE,
                    PetType.EARTH, Favorability.NEUTRAL,
                    PetType.WATER, Favorability.UNFAVORABLE
            ),

            Environment.FOREST,
            Map.of(PetType.EARTH, Favorability.FAVORABLE,
                    PetType.WATER, Favorability.NEUTRAL,
                    PetType.FIRE, Favorability.UNFAVORABLE
            ),

            Environment.OASIS,
            Map.of(PetType.WATER, Favorability.FAVORABLE,
                    PetType.FIRE, Favorability.NEUTRAL,
                    PetType.EARTH, Favorability.UNFAVORABLE
            )

    );


    // Pet internal state

    private final Long id;
    private final Long masterId;
    private final String name;
    private final PetType petType;

    @Setter(AccessLevel.PRIVATE)
    private EnergyLevel energy;

    @Setter(AccessLevel.PRIVATE)
    private Mood mood;

    @Setter(AccessLevel.PRIVATE)
    private PetLevel petLevel;

    @Setter(AccessLevel.PRIVATE)
    private long lastFeedTime;


    // Pet's public API

    public EnergyLevel feed(Food food) {
        if (food == null)
            throw new IllegalArgumentException("Food must be non-null");

        EnergyLevel[] levels = EnergyLevel.values();
        int currentIndex = getEnergy().ordinal();
        int nextIndex = currentIndex + 1;

        setEnergy(nextIndex < levels.length ? levels[nextIndex] : getEnergy());

        return getEnergy();

//         if (!canFeedAgain())
//           return Optional.empty();
//
//        int energyIncrease = food.getEnergyValue();
//
//        Set<FoodType> foodTypes = food.getMacronutrients();
//
//        if (getEnergy() == EnergyLevel.LOWEST) {
//
//            setLastFeedTime(System.currentTimeMillis());
//            setEnergy(EnergyLevel.LOW);
//
//            return Optional.of(EnergyLevel.LOW);
//
//        } else if (getEnergy() == EnergyLevel.LOW) {
//
//            if (energyIncrease >= 2 && hasVariety(foodTypes)) {
//
//                setLastFeedTime(System.currentTimeMillis());
//                setEnergy(EnergyLevel.MEDIUM);
//
//                return Optional.of(EnergyLevel.MEDIUM);
//            }
//
//        } else if (getEnergy() == EnergyLevel.MEDIUM) {
//
//            if (energyIncrease >= 4 && hasVariety(foodTypes)) {
//
//                setLastFeedTime(System.currentTimeMillis());
//                setEnergy(EnergyLevel.HIGH);
//
//                return Optional.of(EnergyLevel.HIGH);
//            }
//
//        } else if (getEnergy() == EnergyLevel.HIGH) {
//
//            if (energyIncrease >= 6 && hasAllTypes(foodTypes)) {
//
//                setLastFeedTime(System.currentTimeMillis());
//                setEnergy(EnergyLevel.HIGHEST);
//
//                return Optional.of(EnergyLevel.HIGHEST);
//            }
//
//        }
//
//        setLastFeedTime(System.currentTimeMillis());
//        return Optional.of(getEnergy());

    }

    public PetLevel evolve(Environment environment) {

        Favorability favorability = determineFavorability(environment);

        if (getPetLevel() == PetLevel.BASIC) {

            if (favorability == Favorability.FAVORABLE &&
                    (getMood() == Mood.CALM || getMood() == Mood.HAPPY) &&
                    getEnergy().compareTo(EnergyLevel.HIGH) >= 0
            ) {

                setPetLevel(PetLevel.SPECIAL_LEVEL_1);

            } else if (favorability != Favorability.UNFAVORABLE && getEnergy().compareTo(EnergyLevel.MEDIUM) >= 0) {

                if (getMood() == Mood.CALM || getMood() == Mood.HAPPY) {

                    setPetLevel(PetLevel.HEROIC_LEVEL_1);

                } else if (getMood() == Mood.ANGRY || getMood() == Mood.SAD) {

                    setPetLevel(PetLevel.DARK_LEVEL_1);

                }

            }

        }

        return getPetLevel();

    }

    public Mood gift(Gift gift) {
        if (gift == null)
            throw new IllegalArgumentException("Gift must be non-null");

        Mood[] moods = Mood.values();
        int currentIndex = getMood().ordinal();
        int nextIndex = currentIndex + 1;

        setMood(nextIndex < moods.length ? moods[nextIndex] : getMood());

        return getMood();

    }


    // Helper methods

    private boolean canFeedAgain() {

        return (System.currentTimeMillis() - lastFeedTime) >= REQUIRED_INTERVAL;

    }

    private boolean hasVariety(Set<FoodType> foodTypes) {
        return foodTypes.size() >= 2;
    }

    private boolean hasAllTypes(Set<FoodType> foodTypes) {

        return foodTypes.contains(FoodType.PROTEIN) &&
                foodTypes.contains(FoodType.CARBOHYDRATE) &&
                foodTypes.contains(FoodType.FAT);

    }

    private Favorability determineFavorability(Environment environment) {
        return FAVORABILITY_MAP
                .getOrDefault(environment, Map.of())
                .getOrDefault(getPetType(), Favorability.NEUTRAL);
    }

}
