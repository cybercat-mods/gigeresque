package mods.cybercat.gigeresque.common.entity.helper;

import net.minecraft.world.phys.Vec3;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.ClassicAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.entity.impl.extra.SpitterEntity;
import mods.cybercat.gigeresque.common.entity.impl.mutant.HammerpedeEntity;
import mods.cybercat.gigeresque.common.entity.impl.mutant.PopperEntity;
import mods.cybercat.gigeresque.common.entity.impl.mutant.StalkerEntity;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerbursterEntity;
import mods.cybercat.gigeresque.interfacing.AnimationSelector;

public record GigMeleeAttackSelector() {

    /* ANIMATION SELECTORS */
    public static final AnimationSelector<ClassicAlienEntity> CLASSIC_ANIM_SELECTOR = classicAlienEntity -> {
        var basicCheck = classicAlienEntity.isCrawling() || classicAlienEntity.isTunnelCrawling() || classicAlienEntity.isInWater();
        var animKey = switch (classicAlienEntity.getRandom().nextInt(4)) {
            case 1 -> basicCheck ? Constants.RIGHT_CLAW_BASIC : Constants.RIGHT_CLAW;
            case 2 -> basicCheck ? Constants.LEFT_TAIL_BASIC : Constants.LEFT_TAIL;
            case 3 -> basicCheck ? Constants.RIGHT_TAIL_BASIC : Constants.RIGHT_TAIL;
            default -> basicCheck ? Constants.LEFT_CLAW_BASIC : Constants.LEFT_CLAW;
        };
        classicAlienEntity.triggerAnim(Constants.ATTACK_CONTROLLER, animKey);
    };

    public static final AnimationSelector<AlienEntity> NORMAL_ANIM_SELECTOR = entity -> {
        var animKey = switch (entity.getRandom().nextInt(4)) {
            case 1 -> Constants.RIGHT_CLAW;
            case 2 -> Constants.LEFT_TAIL;
            case 3 -> Constants.RIGHT_TAIL;
            default -> Constants.LEFT_CLAW;
        };
        entity.triggerAnim(Constants.ATTACK_CONTROLLER, animKey);
    };

    public static final AnimationSelector<StalkerEntity> STALKER_ANIM_SELECTOR = stalker -> {
        var animKey = switch (stalker.getRandom().nextInt(4)) {
            case 1, 3 -> Constants.ATTACK_HEAVY;
            default -> Constants.ATTACK_NORMAL;
        };
        stalker.triggerAnim(Constants.LIVING_CONTROLLER, animKey);
    };

    public static final AnimationSelector<RunnerAlienEntity> RUNNER_ANIM_SELECTOR = runner -> {
        var animKey = switch (runner.getRandom().nextInt(4)) {
            case 1 -> Constants.RIGHT_CLAW;
            case 2 -> Constants.LEFT_TAIL_BASIC;
            case 3 -> Constants.RIGHT_TAIL_BASIC;
            default -> Constants.LEFT_CLAW;
        };
        runner.triggerAnim(Constants.ATTACK_CONTROLLER, animKey);
    };

    public static final AnimationSelector<HammerpedeEntity> HAMMER_ANIM_SELECTOR = hammerpedeEntity -> hammerpedeEntity.triggerAnim(
        Constants.ATTACK_CONTROLLER,
        Constants.ATTACK
    );

    public static final AnimationSelector<RunnerbursterEntity> RBUSTER_ANIM_SELECTOR = runnerbursterEntity -> runnerbursterEntity
        .triggerAnim(
            Constants.LIVING_CONTROLLER,
            Constants.EAT
        );

    public static final AnimationSelector<FacehuggerEntity> HUGGER_SELECTOR = facehuggerEntity -> {
        if (facehuggerEntity.getTarget() != null) {
            var vec3d2 = new Vec3(
                facehuggerEntity.getTarget().getX() - facehuggerEntity.getX(),
                0.0,
                facehuggerEntity.getTarget().getZ() - facehuggerEntity.getZ()
            );
            vec3d2 = vec3d2.normalize().scale(0.2).add(facehuggerEntity.getDeltaMovement().scale(0.2));
            facehuggerEntity.setDeltaMovement(
                vec3d2.x,
                facehuggerEntity.getTarget().getEyeHeight() > 0.8F ? 0.5F : 0.4,
                vec3d2.z
            );
            facehuggerEntity.setJumping(true);
        }
    };

    public static final AnimationSelector<PopperEntity> POPPER_SELECTOR = popperEntity -> {
        if (popperEntity.getTarget() != null) {
            var vec3d2 = new Vec3(
                popperEntity.getTarget().getX() - popperEntity.getX(),
                0.0,
                popperEntity.getTarget().getZ() - popperEntity.getZ()
            );
            vec3d2 = vec3d2.normalize().scale(0.2).add(popperEntity.getDeltaMovement().scale(0.2));
            popperEntity.setDeltaMovement(vec3d2.x, 0.5F, vec3d2.z);
        }
    };

    public static final AnimationSelector<SpitterEntity> SPITTER_RANGE_SELECTOR = spitterEntity -> {
        spitterEntity.triggerAnim(Constants.ATTACK_CONTROLLER, Constants.ACID_SPIT);
    };
}
