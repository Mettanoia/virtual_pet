package com.solafides.virtual_pet_reborn.domain;

public enum EnergyLevel {

    LOWEST, LOW, MEDIUM, HIGH, HIGHEST;

    public int getValue() {
        return switch (this) {
            case LOW -> 1;
            case MEDIUM -> 2;
            case HIGH -> 3;
            case HIGHEST -> 4;
            default -> 0;
        };

    }

}