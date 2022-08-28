package mods.cybercat.gigeresque.common.entity.ai.enums;

import java.util.Map;

import mods.cybercat.gigeresque.common.entity.helper.GenericAlienAttackType;

public enum AlienAttackType {
	NONE(GenericAlienAttackType.NONE), CLAW_LEFT(GenericAlienAttackType.CLAW), CLAW_RIGHT(GenericAlienAttackType.CLAW),
	TAIL_LEFT(GenericAlienAttackType.TAIL), TAIL_RIGHT(GenericAlienAttackType.TAIL),
	CLAW_LEFT_MOVING(GenericAlienAttackType.CLAW), CLAW_RIGHT_MOVING(GenericAlienAttackType.CLAW),
	TAIL_LEFT_MOVING(GenericAlienAttackType.TAIL), TAIL_RIGHT_MOVING(GenericAlienAttackType.TAIL);

	public static final Map<AlienAttackType, String> animationMappings = Map.of(CLAW_LEFT, "left_claw", CLAW_RIGHT,
			"right_claw", TAIL_LEFT, "left_tail", TAIL_RIGHT, "right_tail", CLAW_LEFT_MOVING, "left_claw_moving",
			CLAW_RIGHT_MOVING, "right_claw_moving", TAIL_LEFT_MOVING, "left_tail_moving", TAIL_RIGHT_MOVING,
			"right_tail_moving");

	public final GenericAlienAttackType genericAttackType;

	AlienAttackType(GenericAlienAttackType genericAttackType) {
		this.genericAttackType = genericAttackType;
	}
}
