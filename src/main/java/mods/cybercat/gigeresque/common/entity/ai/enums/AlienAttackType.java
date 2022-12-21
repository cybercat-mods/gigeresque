package mods.cybercat.gigeresque.common.entity.ai.enums;

import java.util.Map;

import mods.cybercat.gigeresque.common.entity.helper.GenericAlienAttackType;

public enum AlienAttackType {
	NONE(GenericAlienAttackType.NONE), CLAW_LEFT(GenericAlienAttackType.CLAW), CLAW_RIGHT(GenericAlienAttackType.CLAW),
	TAIL_LEFT(GenericAlienAttackType.TAIL), TAIL_RIGHT(GenericAlienAttackType.TAIL),
	CLAW_LEFT_MOVING(GenericAlienAttackType.CLAW), CLAW_RIGHT_MOVING(GenericAlienAttackType.CLAW),
	TAIL_LEFT_MOVING(GenericAlienAttackType.TAIL), TAIL_RIGHT_MOVING(GenericAlienAttackType.TAIL),
	NORMAL(GenericAlienAttackType.NORMAL), HEAVY(GenericAlienAttackType.HEAVY);

	public static final Map<AlienAttackType, String> animationMappings = Map.of(CLAW_LEFT, "left_claw_basic",
			CLAW_RIGHT, "right_claw_basic", TAIL_LEFT, "left_tail_basic", TAIL_RIGHT, "right_tail_basic",
			CLAW_LEFT_MOVING, "left_claw", CLAW_RIGHT_MOVING, "right_claw", TAIL_LEFT_MOVING, "left_tail",
			TAIL_RIGHT_MOVING, "right_tail", NORMAL, "attack_normal", HEAVY, "attack_heavy");

	public final GenericAlienAttackType genericAttackType;

	AlienAttackType(GenericAlienAttackType genericAttackType) {
		this.genericAttackType = genericAttackType;
	}
}
