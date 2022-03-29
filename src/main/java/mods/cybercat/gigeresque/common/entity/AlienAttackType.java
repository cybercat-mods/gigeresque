package mods.cybercat.gigeresque.common.entity;

import java.util.Map;

public enum AlienAttackType {
	NONE(GenericAlienAttackType.NONE), CLAW_LEFT(GenericAlienAttackType.CLAW), CLAW_RIGHT(GenericAlienAttackType.CLAW),
	HEAD_BITE(GenericAlienAttackType.BITE), TAIL_LEFT(GenericAlienAttackType.TAIL),
	TAIL_RIGHT(GenericAlienAttackType.TAIL);

	public static final Map<AlienAttackType, String> animationMappings = Map.of(CLAW_LEFT, "left_claw", CLAW_RIGHT,
			"right_claw", HEAD_BITE, "head_bite", TAIL_LEFT, "left_tail", TAIL_RIGHT, "right_tail");

	public final GenericAlienAttackType genericAttackType;

	AlienAttackType(GenericAlienAttackType genericAttackType) {
		this.genericAttackType = genericAttackType;
	}
}
