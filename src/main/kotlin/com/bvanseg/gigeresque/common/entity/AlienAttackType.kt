package com.bvanseg.gigeresque.common.entity

/**
 * @author Boston Vanseghi
 */
enum class AlienAttackType(val genericAttackType: GenericAlienAttackType) {
    NONE(GenericAlienAttackType.NONE),
    CLAW_LEFT(GenericAlienAttackType.CLAW),
    CLAW_RIGHT(GenericAlienAttackType.CLAW),
    HEAD_BITE(GenericAlienAttackType.BITE),
    TAIL_LEFT(GenericAlienAttackType.TAIL),
    TAIL_OVER(GenericAlienAttackType.TAIL),
    TAIL_RIGHT(GenericAlienAttackType.TAIL);

    companion object {
        val animationMappings = mapOf(
            CLAW_LEFT to "attacking_leftclaw",
            CLAW_RIGHT to "attacking_rightclaw",
            HEAD_BITE to "attacking_innerjaw",
            TAIL_LEFT to "attacking_tail_left",
            TAIL_OVER to "attacking_tail_over",
            TAIL_RIGHT to "attacking_tail_right"
        )
    }
}