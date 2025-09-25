package mods.cybercat.gigeresque.common.entity.helper;

import mod.azure.azurelib.rewrite.animation.dispatch.command.AzCommand;
import mod.azure.azurelib.rewrite.animation.play_behavior.AzPlayBehaviors;
import net.minecraft.world.entity.Entity;

import mods.cybercat.gigeresque.Constants;

public class AnimationDispatcher {

    private final AzCommand BIRTH_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "birth", AzPlayBehaviors.PLAY_ONCE);

    private final AzCommand HISS_COMMAND = AzCommand.create(Constants.HISS_CONTROLLER, "hiss", AzPlayBehaviors.PLAY_ONCE);

    private final AzCommand AMBIENT_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "ambient", AzPlayBehaviors.PLAY_ONCE);

    private final AzCommand IDLE_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "idle", AzPlayBehaviors.LOOP);

    private final AzCommand IDLE_LAND_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "idle_land", AzPlayBehaviors.LOOP);

    private final AzCommand IDLE_LAND2_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "idle_land2", AzPlayBehaviors.LOOP);

    private final AzCommand IDLE_WATER_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "idle_water", AzPlayBehaviors.LOOP);

    private final AzCommand ENTER_STASIS_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "stasis_enter", AzPlayBehaviors.PLAY_ONCE);

    private final AzCommand STASIS_LOOP_COMMAND = AzCommand.create(
        Constants.BASE_CONTROLLER,
        "stasis_loop",
        AzPlayBehaviors.LOOP
    );

    // private final AzCommand STATIS_ENTER_COMMAND = AzCommand.compose(ENTER_STASIS_COMMAND, STASIS_LOOP_COMMAND);

    private final AzCommand STATIS_ENTER_COMMAND = AzCommand.controllerBuilder()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue(
                "stasis_enter",
                props -> props.withPlayBehavior(AzPlayBehaviors.PLAY_ONCE)
            ).queue("stasis_loop", props -> props.withPlayBehavior(AzPlayBehaviors.LOOP))

        )
        .setSpeed(Constants.BASE_CONTROLLER, 1.0f)
        .build();

    private final AzCommand STATIS_LEAVE_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "stasis_leave", AzPlayBehaviors.PLAY_ONCE);

    private final AzCommand HATCH_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "hatch", AzPlayBehaviors.LOOP);

    private final AzCommand HATCHED_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "hatched", AzPlayBehaviors.HOLD_ON_LAST_FRAME);

    private final AzCommand ALERT_COMMAND = AzCommand.create(Constants.HOSTILE_CONTROLLER, "alert", AzPlayBehaviors.PLAY_ONCE);

    private final AzCommand PLAY_HOSTILE_COMMAND = AzCommand.create(Constants.HOSTILE_CONTROLLER, "hostile", AzPlayBehaviors.PLAY_ONCE);

    private final AzCommand IDLE_ALERT_COMMAND = AzCommand.create(Constants.HOSTILE_CONTROLLER, "idle_alert", AzPlayBehaviors.PLAY_ONCE);

    private final AzCommand HOSTILE_COMMAND = AzCommand.compose(ALERT_COMMAND, PLAY_HOSTILE_COMMAND, IDLE_ALERT_COMMAND);

    private final AzCommand HATCHING_COMMAND = AzCommand.compose(HATCH_COMMAND, HATCHED_COMMAND);

    private final AzCommand HATCHED_EMPTY_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "hatched_empty", AzPlayBehaviors.LOOP);

    private final AzCommand DEATH_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "death", AzPlayBehaviors.HOLD_ON_LAST_FRAME);

    private final AzCommand HURT_COMMAND = AzCommand.create(Constants.HOSTILE_CONTROLLER, "hurt", AzPlayBehaviors.PLAY_ONCE);

    private final AzCommand MIDAIR_JUMP_COMMAND = AzCommand.create(
        Constants.BASE_CONTROLLER,
        "midair_jump",
        AzPlayBehaviors.PLAY_ONCE
    );

    private final AzCommand MIDAIR_COMMAND = AzCommand.create(
        Constants.BASE_CONTROLLER,
        "midair",
        AzPlayBehaviors.LOOP
    );

    private final AzCommand CHARGE_UP_COMMAND = AzCommand.create(
        Constants.BASE_CONTROLLER,
        "charge",
        AzPlayBehaviors.PLAY_ONCE
    );

    private final AzCommand FACEHUGGER_LUNGE_COMMAND = AzCommand.compose(CHARGE_UP_COMMAND, MIDAIR_JUMP_COMMAND, MIDAIR_COMMAND);

    private final AzCommand IMPREGNATE_COMMAND = AzCommand.create(
        Constants.BASE_CONTROLLER,
        "impregnate",
        AzPlayBehaviors.HOLD_ON_LAST_FRAME
    );

    private final AzCommand STUNNED_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "stunned", AzPlayBehaviors.PLAY_ONCE);

    private final AzCommand SWIM_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "swim", AzPlayBehaviors.LOOP);

    private final AzCommand LUNGE_COMMAND = AzCommand.controllerBuilder()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue(
                "swim",
                props -> props.withPlayBehavior(AzPlayBehaviors.PLAY_ONCE)
            )
        )
        .setSpeed(Constants.BASE_CONTROLLER, 2.5F)
        .build();

    private final AzCommand RUSH_SWIM_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "rush_swim", AzPlayBehaviors.LOOP);

    private final AzCommand RUN_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "run", AzPlayBehaviors.LOOP);

    private final AzCommand RUNNING_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "running", AzPlayBehaviors.LOOP);

    private final AzCommand MOVING_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "moving", AzPlayBehaviors.LOOP);

    private final AzCommand WALK_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "walk", AzPlayBehaviors.LOOP);

    private final AzCommand WALK_HOSTILE_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "walk_hostile", AzPlayBehaviors.LOOP);

    private final AzCommand WALK_CARRYING_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "walk_carrying", AzPlayBehaviors.LOOP);

    private final AzCommand CRAWL_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "crawl", AzPlayBehaviors.LOOP);

    private final AzCommand CRAWL_RUSH_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "rush_crawl", AzPlayBehaviors.LOOP);

    private final AzCommand SLITHER_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "slither", AzPlayBehaviors.LOOP);

    private final AzCommand RUSH_SLITHER_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "rush_slither", AzPlayBehaviors.LOOP);

    private final AzCommand KIDNAP_COMMAND = AzCommand.create(Constants.ATTACK_CONTROLLER, "kidnap", AzPlayBehaviors.LOOP);

    private final AzCommand KIDNAP_IDLE = AzCommand.compose(IDLE_LAND_COMMAND, KIDNAP_COMMAND);

    private final AzCommand UNKIDNAP_COMMAND = AzCommand.create(Constants.ATTACK_CONTROLLER, "unkidnap", AzPlayBehaviors.LOOP);

    private final AzCommand SPIT_COMMAND = AzCommand.create(Constants.ATTACK_CONTROLLER, "spit", AzPlayBehaviors.PLAY_ONCE);

    private final AzCommand SPRAY_COMMAND = AzCommand.create(Constants.ATTACK_CONTROLLER, "spray", AzPlayBehaviors.PLAY_ONCE);

    private final AzCommand CHARGE_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "charge", AzPlayBehaviors.PLAY_ONCE);

    private final AzCommand EAT_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "eat", AzPlayBehaviors.PLAY_ONCE);

    private final AzCommand ATTACK_COMMAND = AzCommand.create(Constants.ATTACK_CONTROLLER, "attack", AzPlayBehaviors.PLAY_ONCE);

    private final AzCommand CHOMP_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "chomp", AzPlayBehaviors.PLAY_ONCE);

    private final AzCommand CHOMP_COMMAND2 = AzCommand.create(Constants.ATTACK_CONTROLLER, "chomp", AzPlayBehaviors.PLAY_ONCE);

    private final AzCommand ATTACK_NORMAL_COMMAND = AzCommand.create(
        Constants.ATTACK_CONTROLLER,
        "attack_normal",
        AzPlayBehaviors.PLAY_ONCE
    );

    private final AzCommand ATTACK_HEAVY_COMMAND = AzCommand.create(Constants.ATTACK_CONTROLLER, "attack_heavy", AzPlayBehaviors.PLAY_ONCE);

    private final AzCommand LEFT_CLAW_COMMAND = AzCommand.create(Constants.ATTACK_CONTROLLER, "left_claw", AzPlayBehaviors.PLAY_ONCE);

    private final AzCommand RIGHT_CLAW_COMMAND = AzCommand.create(Constants.ATTACK_CONTROLLER, "right_claw", AzPlayBehaviors.PLAY_ONCE);

    private final AzCommand LEFT_TAIL_COMMAND = AzCommand.create(Constants.ATTACK_CONTROLLER, "left_tail", AzPlayBehaviors.PLAY_ONCE);

    private final AzCommand RIGHT_TAIL_COMMAND = AzCommand.create(Constants.ATTACK_CONTROLLER, "right_tail", AzPlayBehaviors.PLAY_ONCE);

    private final AzCommand LEFT_CLAW_BASIC_COMMAND = AzCommand.create(
        Constants.ATTACK_CONTROLLER,
        "left_claw_basic",
        AzPlayBehaviors.PLAY_ONCE
    );

    private final AzCommand RIGHT_CLAW_BASIC_COMMAND = AzCommand.create(
        Constants.ATTACK_CONTROLLER,
        "right_claw_basic",
        AzPlayBehaviors.PLAY_ONCE
    );

    private final AzCommand LEFT_CLAW_CRAWLING_COMMAND = AzCommand.create(
        Constants.ATTACK_CONTROLLER,
        "crawl_left_claw",
        AzPlayBehaviors.PLAY_ONCE
    );

    private final AzCommand RIGHT_CLAW_CRAWLING_COMMAND = AzCommand.create(
        Constants.ATTACK_CONTROLLER,
        "crawl_right_claw",
        AzPlayBehaviors.PLAY_ONCE
    );

    private final AzCommand LEFT_TAIL_BASIC_COMMAND = AzCommand.create(
        Constants.ATTACK_CONTROLLER,
        "left_tail_basic",
        AzPlayBehaviors.PLAY_ONCE
    );

    private final AzCommand RIGHT_TAIL_BASIC_COMMAND = AzCommand.create(
        Constants.ATTACK_CONTROLLER,
        "right_tail_basic",
        AzPlayBehaviors.PLAY_ONCE
    );

    private final AzCommand EXECUTION_COMMAND = AzCommand.create(Constants.ATTACK_CONTROLLER, "execution", AzPlayBehaviors.PLAY_ONCE);

    private final AzCommand EXECUTION_CARRY_COMMAND = AzCommand.create(
        Constants.BASE_CONTROLLER,
        "execution_carry",
        AzPlayBehaviors.PLAY_ONCE
    );

    private final AzCommand EXECUTION_GRAB_COMMAND = AzCommand.create(
        Constants.BASE_CONTROLLER,
        "execution_grab",
        AzPlayBehaviors.PLAY_ONCE
    );

    private final AzCommand HATCH_LEAP_COMMAND = AzCommand.create(
        Constants.BASE_CONTROLLER,
        "hatch_leap",
        AzPlayBehaviors.HOLD_ON_LAST_FRAME
    );

    private final AzCommand STAGE_2_COMMAND = AzCommand.create(
        Constants.BASE_CONTROLLER,
        "stagger_walk",
        AzPlayBehaviors.HOLD_ON_LAST_FRAME
    );

    private final AzCommand STAGE_3_COMMAND = AzCommand.create(
        Constants.BASE_CONTROLLER,
        "death_walk",
        AzPlayBehaviors.HOLD_ON_LAST_FRAME
    );

    private final AzCommand STAGE_1_COMMAND = AzCommand.create(
        Constants.BASE_CONTROLLER,
        "limp_walk",
        AzPlayBehaviors.HOLD_ON_LAST_FRAME
    );

    private final Entity animatedEntity;

    public AnimationDispatcher(Entity animatedEntity) {
        this.animatedEntity = animatedEntity;
    }

    public void sendStage1() {
        STAGE_1_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendStage2() {
        STAGE_2_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendStage3() {
        STAGE_3_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendIdle() {
        IDLE_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendIdleLand() {
        IDLE_LAND_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendIdleLand2() {
        IDLE_LAND2_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendIdleWater() {
        IDLE_WATER_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendHatchEmpty() {
        HATCHED_EMPTY_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendHatching() {
        HATCHING_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendDeath() {
        DEATH_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendWalk() {
        WALK_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendRun() {
        RUN_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendRunning() {
        RUNNING_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendImpregate() {
        IMPREGNATE_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendSwim() {
        SWIM_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendRushSwim() {
        RUSH_SWIM_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendCrawl() {
        CRAWL_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendCrawlRush() {
        CRAWL_RUSH_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendSlither() {
        SLITHER_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendRushSlither() {
        RUSH_SLITHER_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendKidnap() {
        KIDNAP_IDLE.sendForEntity(animatedEntity);
    }

    public void sendUnkidnap() {
        UNKIDNAP_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendStunned() {
        STUNNED_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendStatisEnter() {
        STATIS_ENTER_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendStatisLeave() {
        STATIS_LEAVE_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendBirth() {
        BIRTH_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendHiss() {
        HISS_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendAmbient() {
        AMBIENT_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendMoving() {
        MOVING_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendWalkCarrying() {
        WALK_CARRYING_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendWalkHostile() {
        WALK_HOSTILE_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendSpray() {
        SPRAY_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendCharge() {
        CHARGE_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendHatchLeap() {
        HATCH_LEAP_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendChomp() {
        CHOMP_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendChomp2() {
        CHOMP_COMMAND2.sendForEntity(animatedEntity);
    }

    public void sendExecutionCarry() {
        EXECUTION_CARRY_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendExecutionGrab() {
        EXECUTION_GRAB_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendLeftClaw() {
        LEFT_CLAW_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendRightClaw() {
        RIGHT_CLAW_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendLeftTail() {
        LEFT_TAIL_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendRightTail() {
        RIGHT_TAIL_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendLeftClawBasic() {
        LEFT_CLAW_BASIC_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendRightClawBasic() {
        RIGHT_CLAW_BASIC_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendLeftClawCrawling() {
        LEFT_CLAW_CRAWLING_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendRightClawCrawling() {
        RIGHT_CLAW_CRAWLING_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendLeftTailBasic() {
        LEFT_TAIL_BASIC_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendRightTailBasic() {
        RIGHT_TAIL_BASIC_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendExecution() {
        EXECUTION_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendEat() {
        EAT_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendHostile() {
        HOSTILE_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendHurt() {
        HURT_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendNormal() {
        ATTACK_NORMAL_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendHeavy() {
        ATTACK_HEAVY_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendAttack() {
        ATTACK_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendAcidSpit() {
        SPIT_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendStasisLoop() {
        STASIS_LOOP_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendLunge() {
        LUNGE_COMMAND.sendForEntity(animatedEntity);
    }

    public void sendFacehuggerLunge() {
        FACEHUGGER_LUNGE_COMMAND.sendForEntity(animatedEntity);
    }
}
