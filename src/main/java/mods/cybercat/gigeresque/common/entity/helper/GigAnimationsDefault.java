package mods.cybercat.gigeresque.common.entity.helper;

import mod.azure.azurelib.core.animation.Animation.LoopType;
import mod.azure.azurelib.core.animation.RawAnimation;

public record GigAnimationsDefault() {

    public static final RawAnimation BIRTH = RawAnimation.begin().then("birth", LoopType.PLAY_ONCE);
    public static final RawAnimation HISS = RawAnimation.begin().then("hiss", LoopType.PLAY_ONCE);
    public static final RawAnimation AMBIENT = RawAnimation.begin().then("ambient", LoopType.PLAY_ONCE);

    /* LAND IDLE */
    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation IDLE_LAND = RawAnimation.begin().thenLoop("idle_land");
    public static final RawAnimation IDLE_LAND2 = RawAnimation.begin().thenLoop("idle_land2");
    public static final RawAnimation STATIS_ENTER = RawAnimation.begin().then("stasis_enter",
            LoopType.PLAY_ONCE).thenLoop("stasis_loop");
    public static final RawAnimation STATIS_LOOP = RawAnimation.begin().thenLoop("stasis_loop");
    public static final RawAnimation STATIS_LEAVE = RawAnimation.begin().then("stasis_leave", LoopType.PLAY_ONCE);

    /* WATER IDLE */
    public static final RawAnimation IDLE_WATER = RawAnimation.begin().thenLoop("idle_water");

    /* MOVEMENT */
    public static final RawAnimation SWIM = RawAnimation.begin().thenLoop("swim");
    public static final RawAnimation RUSH_SWIM = RawAnimation.begin().thenLoop("rush_swim");
    public static final RawAnimation RUN = RawAnimation.begin().thenLoop("run");
    public static final RawAnimation RUNNING = RawAnimation.begin().thenLoop("running");
    public static final RawAnimation MOVING = RawAnimation.begin().thenLoop("moving");
    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    public static final RawAnimation WALK_HOSTILE = RawAnimation.begin().thenLoop("walk_hostile");
    public static final RawAnimation WALK_CARRYING = RawAnimation.begin().thenLoop("walk_carrying");
    public static final RawAnimation CRAWL = RawAnimation.begin().thenLoop("crawl");
    public static final RawAnimation CRAWL_RUSH = RawAnimation.begin().thenLoop("rush_crawl");
    public static final RawAnimation SLITHER = RawAnimation.begin().thenLoop("slither");
    public static final RawAnimation RUSH_SLITHER = RawAnimation.begin().thenLoop("rush_slither");

    /* EGG ANIMATIONS */
    public static final RawAnimation HATCHING = RawAnimation.begin().then("hatch", LoopType.PLAY_ONCE).thenPlayAndHold(
            "hatched");
    public static final RawAnimation HATCHED = RawAnimation.begin().thenLoop("hatched");
    public static final RawAnimation HATCHED_EMPTY = RawAnimation.begin().thenLoop("hatched_empty");
    public static final RawAnimation HATCH_LEAP = RawAnimation.begin().thenPlayAndHold("hatch_leap");

    /* ATTACKING */
    public static final RawAnimation SPIT = RawAnimation.begin().then("spit", LoopType.PLAY_ONCE);
    public static final RawAnimation SPRAY = RawAnimation.begin().then("spray", LoopType.PLAY_ONCE);
    public static final RawAnimation CHARGE = RawAnimation.begin().then("charge", LoopType.PLAY_ONCE);
    public static final RawAnimation ATTACK = RawAnimation.begin().then("attack", LoopType.PLAY_ONCE);
    public static final RawAnimation DEATH = RawAnimation.begin().thenPlayAndHold("death");
    public static final RawAnimation IMPREGNATE = RawAnimation.begin().thenPlayAndHold("impregnate");
    public static final RawAnimation CHOMP = RawAnimation.begin().then("chomp", LoopType.PLAY_ONCE);
    public static final RawAnimation ATTACK_NORMAL = RawAnimation.begin().then("attack_normal", LoopType.PLAY_ONCE);
    public static final RawAnimation ATTACK_HEAVY = RawAnimation.begin().then("attack_heavy", LoopType.PLAY_ONCE);
    public static final RawAnimation LEFT_CLAW = RawAnimation.begin().then("left_claw", LoopType.PLAY_ONCE);
    public static final RawAnimation RIGHT_CLAW = RawAnimation.begin().then("right_claw", LoopType.PLAY_ONCE);
    public static final RawAnimation LEFT_TAIL = RawAnimation.begin().then("left_tail", LoopType.PLAY_ONCE);
    public static final RawAnimation RIGHT_TAIL = RawAnimation.begin().then("right_tail", LoopType.PLAY_ONCE);
    public static final RawAnimation LEFT_CLAW_BASIC = RawAnimation.begin().then("left_claw_basic", LoopType.PLAY_ONCE);
    public static final RawAnimation RIGHT_CLAW_BASIC = RawAnimation.begin().then("right_claw_basic",
            LoopType.PLAY_ONCE);
    public static final RawAnimation LEFT_TAIL_BASIC = RawAnimation.begin().then("left_tail_basic", LoopType.PLAY_ONCE);
    public static final RawAnimation RIGHT_TAIL_BASIC = RawAnimation.begin().then("right_tail_basic",
            LoopType.PLAY_ONCE);
    public static final RawAnimation KIDNAP = RawAnimation.begin().thenLoop("kidnap");
    public static final RawAnimation EXECUTION_CARRY = RawAnimation.begin().thenPlayXTimes("execution_carry", 1);
    public static final RawAnimation EXECUTION_GRAB = RawAnimation.begin().thenPlayXTimes("execution_grab", 1);
}
