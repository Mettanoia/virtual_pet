package com.solafides.virtual_pet_reborn.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gift {

    BONE(1),
    TOY(2),
    BALL(3);

    private final int moodImpact;

}
