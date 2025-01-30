package mods.cybercat.gigeresque.common.entity.helper;

import mod.azure.azurelib.rewrite.animation.dispatch.command.AzCommand;
import mod.azure.azurelib.rewrite.animation.play_behavior.AzPlayBehaviors;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.NewAlienEntity;

public class AnimationDispatcher {

    public final AzCommand BIRTH_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "birth", AzPlayBehaviors.PLAY_ONCE);

    public final AzCommand HISS_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "hiss", AzPlayBehaviors.PLAY_ONCE);

    public final AzCommand AMBIENT_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "ambient", AzPlayBehaviors.PLAY_ONCE);

    public final AzCommand IDLE_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "idle", AzPlayBehaviors.LOOP);

    public final AzCommand IDLE_LAND_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "idle_land", AzPlayBehaviors.LOOP);

    public final AzCommand IDLE_LAND2_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "idle_land2", AzPlayBehaviors.LOOP);

    public final AzCommand IDLE_WATER_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "idle_water", AzPlayBehaviors.LOOP);

    public final AzCommand STATIS_ENTER_COMMAND = AzCommand.builder()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("stasis_enter", props -> props.withPlayBehavior(AzPlayBehaviors.PLAY_ONCE))
                .queue("stasis_loop", props -> props.withPlayBehavior(AzPlayBehaviors.LOOP))
        )
        .build();

    public final AzCommand STATIS_LEAVE_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "stasis_leave", AzPlayBehaviors.PLAY_ONCE);

    public final AzCommand HATCHING_COMMAND = AzCommand.builder()
        .playSequence(
            Constants.BASE_CONTROLLER,
            sequenceBuilder -> sequenceBuilder.queue("hatch", props -> props.withPlayBehavior(AzPlayBehaviors.PLAY_ONCE))
                .queue("hatched", props -> props.withPlayBehavior(AzPlayBehaviors.HOLD_ON_LAST_FRAME))
        )
        .build();

    public final AzCommand HATCHED_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "hatched", AzPlayBehaviors.LOOP);

    public final AzCommand HATCHED_EMPTY_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "hatched_empty", AzPlayBehaviors.LOOP);

    public final AzCommand DEATH_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "death", AzPlayBehaviors.HOLD_ON_LAST_FRAME);

    public final AzCommand IMPREGNATE_COMMAND = AzCommand.create(
        Constants.BASE_CONTROLLER,
        "impregnate",
        AzPlayBehaviors.HOLD_ON_LAST_FRAME
    );

    public final AzCommand STUNNED_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "stunned", AzPlayBehaviors.PLAY_ONCE);

    public final AzCommand SWIM_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "swim", AzPlayBehaviors.LOOP);

    public final AzCommand RUSH_SWIM_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "rush_swim", AzPlayBehaviors.LOOP);

    public final AzCommand RUN_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "run", AzPlayBehaviors.LOOP);

    public final AzCommand RUNNING_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "running", AzPlayBehaviors.LOOP);

    public final AzCommand MOVING_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "moving", AzPlayBehaviors.LOOP);

    public final AzCommand WALK_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "walk", AzPlayBehaviors.LOOP);

    public final AzCommand WALK_HOSTILE_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "walk_hostile", AzPlayBehaviors.LOOP);

    public final AzCommand WALK_CARRYING_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "walk_carrying", AzPlayBehaviors.LOOP);

    public final AzCommand CRAWL_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "crawl", AzPlayBehaviors.LOOP);

    public final AzCommand CRAWL_RUSH_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "rush_crawl", AzPlayBehaviors.LOOP);

    public final AzCommand SLITHER_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "slither", AzPlayBehaviors.LOOP);

    public final AzCommand RUSH_SLITHER_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "rush_slither", AzPlayBehaviors.LOOP);

    public final AzCommand KIDNAP_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "kidnap", AzPlayBehaviors.LOOP);

    public final AzCommand SPIT_COMMAND = AzCommand.create(Constants.ATTACK_CONTROLLER, "spit", AzPlayBehaviors.PLAY_ONCE);

    public final AzCommand SPRAY_COMMAND = AzCommand.create(Constants.ATTACK_CONTROLLER, "spray", AzPlayBehaviors.PLAY_ONCE);

    public final AzCommand CHARGE_COMMAND = AzCommand.create(Constants.ATTACK_CONTROLLER, "charge", AzPlayBehaviors.PLAY_ONCE);

    public final AzCommand ATTACK_COMMAND = AzCommand.create(Constants.ATTACK_CONTROLLER, "attack", AzPlayBehaviors.PLAY_ONCE);

    public final AzCommand CHOMP_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "chomp", AzPlayBehaviors.PLAY_ONCE);

    public final AzCommand ATTACK_NORMAL_COMMAND = AzCommand.create(Constants.ATTACK_CONTROLLER, "attack_normal", AzPlayBehaviors.PLAY_ONCE);

    public final AzCommand ATTACK_HEAVY_COMMAND = AzCommand.create(Constants.ATTACK_CONTROLLER, "attack_heavy", AzPlayBehaviors.PLAY_ONCE);

    public final AzCommand LEFT_CLAW_COMMAND = AzCommand.create(Constants.ATTACK_CONTROLLER, "left_claw", AzPlayBehaviors.PLAY_ONCE);

    public final AzCommand RIGHT_CLAW_COMMAND = AzCommand.create(Constants.ATTACK_CONTROLLER, "right_claw", AzPlayBehaviors.PLAY_ONCE);

    public final AzCommand LEFT_TAIL_COMMAND = AzCommand.create(Constants.ATTACK_CONTROLLER, "left_tail", AzPlayBehaviors.PLAY_ONCE);

    public final AzCommand RIGHT_TAIL_COMMAND = AzCommand.create(Constants.ATTACK_CONTROLLER, "right_tail", AzPlayBehaviors.PLAY_ONCE);

    public final AzCommand LEFT_CLAW_BASIC_COMMAND = AzCommand.create(
        Constants.ATTACK_CONTROLLER,
        "left_claw_basic",
        AzPlayBehaviors.PLAY_ONCE
    );

    public final AzCommand RIGHT_CLAW_BASIC_COMMAND = AzCommand.create(
        Constants.ATTACK_CONTROLLER,
        "right_claw_basic",
        AzPlayBehaviors.PLAY_ONCE
    );

    public final AzCommand LEFT_TAIL_BASIC_COMMAND = AzCommand.create(
        Constants.ATTACK_CONTROLLER,
        "left_tail_basic",
        AzPlayBehaviors.PLAY_ONCE
    );

    public final AzCommand RIGHT_TAIL_BASIC_COMMAND = AzCommand.create(
        Constants.ATTACK_CONTROLLER,
        "right_tail_basic",
        AzPlayBehaviors.PLAY_ONCE
    );

    public final AzCommand EXECUTION_COMMAND = AzCommand.create(Constants.ATTACK_CONTROLLER, "execution", AzPlayBehaviors.PLAY_ONCE);

    public final AzCommand EXECUTION_CARRY_COMMAND = AzCommand.create(
        Constants.BASE_CONTROLLER,
        "execution_carry",
        AzPlayBehaviors.PLAY_ONCE
    );

    public final AzCommand EXECUTION_GRAB_COMMAND = AzCommand.create(
        Constants.BASE_CONTROLLER,
        "execution_grab",
        AzPlayBehaviors.PLAY_ONCE
    );

    public final AzCommand HATCH_LEAP_COMMAND = AzCommand.create(
        Constants.BASE_CONTROLLER,
        "hatch_leap",
        AzPlayBehaviors.HOLD_ON_LAST_FRAME
    );

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

    public void sendChomp() {
        CHOMP_COMMAND.sendForEntity(animatedEntity);
    }
}
