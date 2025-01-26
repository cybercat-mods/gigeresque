package mods.cybercat.gigeresque.common.entity.helper;

import mod.azure.azurelib.rewrite.animation.dispatch.command.AzCommand;
import mod.azure.azurelib.rewrite.animation.play_behavior.AzPlayBehaviors;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.NewAlienEntity;

public class AnimationDispatcher {

    public final AzCommand BIRTH_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("birth", props -> props.withPlayBehavior(AzPlayBehaviors.PLAY_ONCE))
        )
        .build();

    public final AzCommand HISS_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("hiss", props -> props.withPlayBehavior(AzPlayBehaviors.PLAY_ONCE))
        )
        .build();

    public final AzCommand AMBIENT_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("ambient", props -> props.withPlayBehavior(AzPlayBehaviors.PLAY_ONCE))
        )
        .build();

    public final AzCommand IDLE_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("idle", props -> props.withPlayBehavior(AzPlayBehaviors.LOOP))
        )
        .build();

    public final AzCommand IDLE_LAND_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("idle_land", props -> props.withPlayBehavior(AzPlayBehaviors.LOOP))
        )
        .build();

    public final AzCommand IDLE_LAND2_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("idle_land2", props -> props.withPlayBehavior(AzPlayBehaviors.LOOP))
        )
        .build();

    public final AzCommand IDLE_WATER_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("idle_water", props -> props.withPlayBehavior(AzPlayBehaviors.LOOP))
        )
        .build();

    public final AzCommand STATIS_ENTER_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("stasis_enter", props -> props.withPlayBehavior(AzPlayBehaviors.PLAY_ONCE))
                .queue("stasis_loop", props -> props.withPlayBehavior(AzPlayBehaviors.LOOP))
        )
        .build();

    public final AzCommand STATIS_LEAVE_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("stasis_leave", props -> props.withPlayBehavior(AzPlayBehaviors.PLAY_ONCE))
        )
        .build();

    public final AzCommand HATCHING_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("hatch", props -> props.withPlayBehavior(AzPlayBehaviors.PLAY_ONCE))
                .queue("hatched", props -> props.withPlayBehavior(AzPlayBehaviors.HOLD_ON_LAST_FRAME))
        )
        .build();

    public final AzCommand HATCHED_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("hatched", props -> props.withPlayBehavior(AzPlayBehaviors.LOOP))
        )
        .build();

    public final AzCommand HATCHED_EMPTY_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("hatched_empty", props -> props.withPlayBehavior(AzPlayBehaviors.LOOP))
        )
        .build();

    public final AzCommand DEATH_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("death", props -> props.withPlayBehavior(AzPlayBehaviors.HOLD_ON_LAST_FRAME))
        )
        .build();

    public final AzCommand IMPREGNATE_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("impregnate", props -> props.withPlayBehavior(AzPlayBehaviors.HOLD_ON_LAST_FRAME))
        )
        .build();

    public final AzCommand STUNNED_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("stunned", props -> props.withPlayBehavior(AzPlayBehaviors.PLAY_ONCE))
        )
        .build();

    public final AzCommand SWIM_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("swim", props -> props.withPlayBehavior(AzPlayBehaviors.LOOP))
        )
        .build();

    public final AzCommand RUSH_SWIM_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("rush_swim", props -> props.withPlayBehavior(AzPlayBehaviors.LOOP))
        )
        .build();

    public final AzCommand RUN_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("run", props -> props.withPlayBehavior(AzPlayBehaviors.LOOP))
        )
        .build();

    public final AzCommand RUNNING_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("running", props -> props.withPlayBehavior(AzPlayBehaviors.LOOP))
        )
        .build();

    public final AzCommand MOVING_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("moving", props -> props.withPlayBehavior(AzPlayBehaviors.LOOP))
        )
        .build();

    public final AzCommand WALK_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("walk", props -> props.withPlayBehavior(AzPlayBehaviors.LOOP))
        )
        .build();

    public final AzCommand WALK_HOSTILE_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("walk_hostile", props -> props.withPlayBehavior(AzPlayBehaviors.LOOP))
        )
        .build();

    public final AzCommand WALK_CARRYING_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("walk_carrying", props -> props.withPlayBehavior(AzPlayBehaviors.LOOP))
        )
        .build();

    public final AzCommand CRAWL_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("crawl", props -> props.withPlayBehavior(AzPlayBehaviors.LOOP))
        )
        .build();

    public final AzCommand CRAWL_RUSH_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("rush_crawl", props -> props.withPlayBehavior(AzPlayBehaviors.LOOP))
        )
        .build();

    public final AzCommand SLITHER_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("slither", props -> props.withPlayBehavior(AzPlayBehaviors.LOOP))
        )
        .build();

    public final AzCommand RUSH_SLITHER_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("rush_slither", props -> props.withPlayBehavior(AzPlayBehaviors.LOOP))
        )
        .build();

    public final AzCommand KIDNAP_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("kidnap", props -> props.withPlayBehavior(AzPlayBehaviors.LOOP))
        )
        .build();

    public final AzCommand SPIT_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("spit", props -> props.withPlayBehavior(AzPlayBehaviors.PLAY_ONCE))
        )
        .build();

    public final AzCommand SPRAY_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("spray", props -> props.withPlayBehavior(AzPlayBehaviors.PLAY_ONCE))
        )
        .build();

    public final AzCommand CHARGE_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("charge", props -> props.withPlayBehavior(AzPlayBehaviors.PLAY_ONCE))
        )
        .build();

    public final AzCommand ATTACK_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("attack", props -> props.withPlayBehavior(AzPlayBehaviors.PLAY_ONCE))
        )
        .build();

    public final AzCommand CHOMP_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("chomp", props -> props.withPlayBehavior(AzPlayBehaviors.PLAY_ONCE))
        )
        .build();

    public final AzCommand ATTACK_NORMAL_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("attack_normal", props -> props.withPlayBehavior(AzPlayBehaviors.PLAY_ONCE))
        )
        .build();

    public final AzCommand ATTACK_HEAVY_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("attack_heavy", props -> props.withPlayBehavior(AzPlayBehaviors.PLAY_ONCE))
        )
        .build();

    public final AzCommand LEFT_CLAW_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("left_claw", props -> props.withPlayBehavior(AzPlayBehaviors.PLAY_ONCE))
        )
        .build();

    public final AzCommand RIGHT_CLAW_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("right_claw", props -> props.withPlayBehavior(AzPlayBehaviors.PLAY_ONCE))
        )
        .build();

    public final AzCommand LEFT_TAIL_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("left_tail", props -> props.withPlayBehavior(AzPlayBehaviors.PLAY_ONCE))
        )
        .build();

    public final AzCommand RIGHT_TAIL_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("right_tail", props -> props.withPlayBehavior(AzPlayBehaviors.PLAY_ONCE))
        )
        .build();

    public final AzCommand LEFT_CLAW_BASIC_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("left_claw_basic", props -> props.withPlayBehavior(AzPlayBehaviors.PLAY_ONCE))
        )
        .build();

    public final AzCommand RIGHT_CLAW_BASIC_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("right_claw_basic", props -> props.withPlayBehavior(AzPlayBehaviors.PLAY_ONCE))
        )
        .build();

    public final AzCommand LEFT_TAIL_BASIC_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("left_tail_basic", props -> props.withPlayBehavior(AzPlayBehaviors.PLAY_ONCE))
        )
        .build();

    public final AzCommand RIGHT_TAIL_BASIC_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("right_tail_basic", props -> props.withPlayBehavior(AzPlayBehaviors.PLAY_ONCE))
        )
        .build();

    public final AzCommand EXECUTION_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("execution", props -> props.withPlayBehavior(AzPlayBehaviors.PLAY_ONCE))
        )
        .build();

    public final AzCommand EXECUTION_CARRY_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("execution_carry", props -> props.withPlayBehavior(AzPlayBehaviors.PLAY_ONCE))
        )
        .build();

    public final AzCommand EXECUTION_GRAB_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("execution_grab", props -> props.withPlayBehavior(AzPlayBehaviors.PLAY_ONCE))
        )
        .build();

    public final AzCommand HATCH_LEAP_COMMAND = AzCommand.builder()
        .cancelAll()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("hatch_leap", props -> props.withPlayBehavior(AzPlayBehaviors.PLAY_ONCE))
        )
        .build();

    public final NewAlienEntity animatedEntity;

    public AnimationDispatcher(NewAlienEntity animatedEntity) {
        this.animatedEntity = animatedEntity;
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

    public void sendHatched() {
        HATCHED_COMMAND.sendForEntity(animatedEntity);
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
        KIDNAP_COMMAND.sendForEntity(animatedEntity);
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
}
