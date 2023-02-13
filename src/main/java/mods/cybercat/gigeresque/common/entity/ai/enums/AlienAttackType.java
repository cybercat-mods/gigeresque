package mods.cybercat.gigeresque.common.entity.ai.enums;

import java.util.Map;

import mods.cybercat.gigeresque.common.entity.helper.GenericAlienAttackType;

public enum AlienAttackType {
	NONE(GenericAlienAttackType.NONE), CLAW_LEFT(GenericAlienAttackType.CLAW), CLAW_RIGHT(GenericAlienAttackType.CLAW),
	TAIL_LEFT(GenericAlienAttackType.TAIL), TAIL_RIGHT(GenericAlienAttackType.TAIL),
	CLAW_LEFT_MOVING(GenericAlienAttackType.CLAW), CLAW_RIGHT_MOVING(GenericAlienAttackType.CLAW),
	TAIL_LEFT_MOVING(GenericAlienAttackType.TAIL), TAIL_RIGHT_MOVING(GenericAlienAttackType.TAIL),
	NORMAL(GenericAlienAttackType.NORMAL), HEAVY(GenericAlienAttackType.HEAVY),
	EXECUTION(GenericAlienAttackType.EXECUTION);

	public static final Map<AlienAttackType, String> animationMappings = Map.ofEntries(
			Map.entry(CLAW_LEFT, "left_claw_basic"), Map.entry(CLAW_RIGHT, "right_claw_basic"),
			Map.entry(TAIL_LEFT, "left_tail_basic"), Map.entry(TAIL_RIGHT, "right_tail_basic"),
			Map.entry(CLAW_LEFT_MOVING, "left_claw"), Map.entry(CLAW_RIGHT_MOVING, "right_claw"),
			Map.entry(TAIL_LEFT_MOVING, "left_tail"), Map.entry(TAIL_RIGHT_MOVING, "right_tail"),
			Map.entry(NORMAL, "attack_normal"), Map.entry(HEAVY, "attack_heavy"), Map.entry(EXECUTION, "execution"));

	public final GenericAlienAttackType genericAttackType;

	AlienAttackType(GenericAlienAttackType genericAttackType) {
		this.genericAttackType = genericAttackType;
	}
}
