package com.bvanseg.gigeresque.common.entity;

import java.util.Map;

public enum AlienAttackTypeJava {
    NONE(GenericAlienAttackTypeJava.NONE),
    CLAW_LEFT(GenericAlienAttackTypeJava.CLAW),
    CLAW_RIGHT(GenericAlienAttackTypeJava.CLAW),
    HEAD_BITE(GenericAlienAttackTypeJava.BITE),
    TAIL_LEFT(GenericAlienAttackTypeJava.TAIL),
    TAIL_OVER(GenericAlienAttackTypeJava.TAIL),
    TAIL_RIGHT(GenericAlienAttackTypeJava.TAIL);

    public static final Map<AlienAttackTypeJava, String> animationMappings = Map.of(
            CLAW_LEFT, "attacking_leftclaw",
            CLAW_RIGHT, "attacking_rightclaw",
            HEAD_BITE, "attacking_innerjaw",
            TAIL_LEFT, "attacking_tail_left",
            TAIL_OVER, "attacking_tail_over",
            TAIL_RIGHT, "attacking_tail_right"
    );

    public final GenericAlienAttackTypeJava genericAlienAttackType;

    AlienAttackTypeJava(GenericAlienAttackTypeJava genericAlienAttackType) {
        this.genericAlienAttackType = genericAlienAttackType;
    }
}
