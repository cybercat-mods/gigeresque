package mods.cybercat.gigeresque.common.entity.helper;

import mod.azure.azurelib.core.animation.Animation.LoopType;
import mod.azure.azurelib.core.animation.RawAnimation;

public class GigAnimationsDefault {

	public static final RawAnimation BIRTH = RawAnimation.begin().then("birth", LoopType.PLAY_ONCE);
	public static final RawAnimation HISS = RawAnimation.begin().thenPlayXTimes("hiss", 1);
	public static final RawAnimation AMBIENT = RawAnimation.begin().thenPlayXTimes("ambient", 1);

	/* LAND IDLE */
	public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
	public static final RawAnimation IDLE_LAND = RawAnimation.begin().thenLoop("idle_land");
	public static final RawAnimation IDLE_LAND2 = RawAnimation.begin().thenLoop("idle_land2");
	public static final RawAnimation STATIS_ENTER = RawAnimation.begin().thenPlayAndHold("statis_enter");
	public static final RawAnimation STATIS_LEAVE = RawAnimation.begin().thenPlayXTimes("statis_leave", 1);

	/* WATER IDLE */
	public static final RawAnimation IDLE_WATER = RawAnimation.begin().thenLoop("idle_water");

	/* MOVEMENT */
	public static final RawAnimation SWIM = RawAnimation.begin().thenLoop("swim");
	public static final RawAnimation RUSH_SWIM = RawAnimation.begin().thenLoop("rush_swim");
	public static final RawAnimation RUN = RawAnimation.begin().thenLoop("run");
	public static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
	public static final RawAnimation WALK_HOSTILE = RawAnimation.begin().thenLoop("walk_hostile");
	public static final RawAnimation WALK_CARRYING = RawAnimation.begin().thenLoop("walk_carrying");
	public static final RawAnimation CRAWL = RawAnimation.begin().thenLoop("crawl");
	public static final RawAnimation CRAWL_RUSH = RawAnimation.begin().thenLoop("rush_crawl");
	public static final RawAnimation SLITHER = RawAnimation.begin().thenLoop("slither");
	public static final RawAnimation RUSH_SLITHER = RawAnimation.begin().thenLoop("rush_slither");

	/* EGG ANIMATIONS */
	public static final RawAnimation HATCHING = RawAnimation.begin().then("hatch", LoopType.PLAY_ONCE)
			.thenPlayAndHold("hatched");
	public static final RawAnimation HATCHED = RawAnimation.begin().thenLoop("hatched");
	public static final RawAnimation HATCHED_EMPTY = RawAnimation.begin().thenLoop("hatched_empty");
	public static final RawAnimation HATCH_LEAP = RawAnimation.begin().then("hatch_leap", LoopType.PLAY_ONCE);

	/* ATTACKING */
	public static final RawAnimation CHARGE = RawAnimation.begin().thenPlayAndHold("charge");
	public static final RawAnimation DEATH = RawAnimation.begin().thenPlayAndHold("death");
	public static final RawAnimation IMPREGNATE = RawAnimation.begin().thenPlayAndHold("impregnate");
	public static final RawAnimation CHOMP = RawAnimation.begin().then("chomp", LoopType.PLAY_ONCE);
	public static final RawAnimation LEFT_CLAW = RawAnimation.begin().then("left_claw", LoopType.PLAY_ONCE);
	public static final RawAnimation KIDNAP = RawAnimation.begin().thenLoop("kidnap");
	public static final RawAnimation EXECUTION_CARRY = RawAnimation.begin().thenPlayXTimes("execution_carry", 1);
	public static final RawAnimation EXECUTION_GRAB = RawAnimation.begin().thenPlayXTimes("execution_grab", 1);
}
