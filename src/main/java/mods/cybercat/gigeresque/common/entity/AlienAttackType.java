package mods.cybercat.gigeresque.common.entity;

import java.util.Map;

public enum AlienAttackType {
	NONE(GenericAlienAttackType.NONE), CLAW_LEFT(GenericAlienAttackType.CLAW), CLAW_RIGHT(GenericAlienAttackType.CLAW),
	HEAD_BITE(GenericAlienAttackType.BITE), TAIL_LEFT(GenericAlienAttackType.TAIL),
	TAIL_OVER(GenericAlienAttackType.TAIL), TAIL_RIGHT(GenericAlienAttackType.TAIL);

	public static final Map<AlienAttackType, String> animationMappings = Map.of(CLAW_LEFT, "attacking_leftclaw",
			CLAW_RIGHT, "attacking_rightclaw", HEAD_BITE, "attacking_innerjaw", TAIL_LEFT, "attacking_tail_left",
			TAIL_OVER, "attacking_tail_over", TAIL_RIGHT, "attacking_tail_right");

	public final GenericAlienAttackType genericAttackType;

	AlienAttackType(GenericAlienAttackType genericAttackType) {
		this.genericAttackType = genericAttackType;
	}
}
