package mods.cybercat.gigeresque.common.entity.helper;

import net.minecraft.world.phys.Vec3;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.ChestbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.entity.impl.misc.SpitterEntity;
import mods.cybercat.gigeresque.common.entity.impl.mutant.HammerpedeEntity;
import mods.cybercat.gigeresque.common.entity.impl.mutant.PopperEntity;
import mods.cybercat.gigeresque.common.entity.impl.mutant.StalkerEntity;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerbursterEntity;
import mods.cybercat.gigeresque.interfacing.AnimationSelector;

public record GigMeleeAttackSelector() {

    /* ANIMATION SELECTORS */
    public static final AnimationSelector<AlienEntity> CLASSIC_ANIM_SELECTOR = classicAlienEntity -> {
        var isCrawling = classicAlienEntity.crawlingManager.isCrawling();
        var isInWater = classicAlienEntity.isInWater();
        var dispatcher = classicAlienEntity.animationDispatcher;

        Runnable animKey = switch (classicAlienEntity.getRandom().nextInt(4)) {
            case 1 -> isCrawling
                ? dispatcher::sendRightClawCrawling
                : isInWater
                    ? dispatcher::sendRightClawBasic
                    : dispatcher::sendRightClaw;
            case 2 -> isCrawling
                ? dispatcher::sendLeftClawCrawling
                : isInWater
                    ? dispatcher::sendLeftTailBasic
                    : dispatcher::sendLeftTail;
            case 3 -> isCrawling
                ? dispatcher::sendRightClawCrawling
                : isInWater
                    ? dispatcher::sendRightTailBasic
                    : dispatcher::sendRightTail;
            default -> isCrawling
                ? dispatcher::sendLeftClawCrawling
                : isInWater
                    ? dispatcher::sendLeftClawBasic
                    : dispatcher::sendLeftClaw;
        };

        GigCommonMethods.setAnimation(animKey);

    };

    public static final AnimationSelector<AlienEntity> NORMAL_ANIM_SELECTOR = entity -> {
        var basicCheck = entity.crawlingManager.isCrawling() || entity.isInWater();
        Runnable animKey = switch (entity.getRandom().nextInt(4)) {
            case 1 -> entity.animationDispatcher::sendRightClaw;
            case 2 -> basicCheck ? entity.animationDispatcher::sendRightClaw : entity.animationDispatcher::sendLeftTail;
            case 3 -> basicCheck ? entity.animationDispatcher::sendLeftClaw : entity.animationDispatcher::sendRightTail;
            default -> entity.animationDispatcher::sendLeftClaw;
        };
        GigCommonMethods.setAnimation(animKey);
    };

    public static final AnimationSelector<StalkerEntity> STALKER_ANIM_SELECTOR = stalker -> {
        Runnable animKey = switch (stalker.getRandom().nextInt(4)) {
            case 1, 3 -> stalker.animationDispatcher::sendHeavy;
            default -> stalker.animationDispatcher::sendNormal;
        };
        GigCommonMethods.setAnimation(animKey);
    };

    public static final AnimationSelector<AlienEntity> STANDARD_ANIM_SELECTOR = templeBeastEntity -> {
        var basicCheck = templeBeastEntity.crawlingManager.isCrawling();
        Runnable animKey = switch (templeBeastEntity.getRandom().nextInt(4)) {
            case 1 -> templeBeastEntity.animationDispatcher::sendRightClaw;
            case 2 -> basicCheck
                ? templeBeastEntity.animationDispatcher::sendLeftClaw
                : templeBeastEntity.animationDispatcher::sendLeftTail;
            case 3 -> basicCheck
                ? templeBeastEntity.animationDispatcher::sendRightClaw
                : templeBeastEntity.animationDispatcher::sendRightTail;
            default -> templeBeastEntity.animationDispatcher::sendLeftClaw;
        };
        GigCommonMethods.setAnimation(animKey);
    };

    public static final AnimationSelector<HammerpedeEntity> HAMMER_ANIM_SELECTOR = hammerpedeEntity -> hammerpedeEntity.animationDispatcher
        .sendAttack();

    public static final AnimationSelector<RunnerbursterEntity> RBUSTER_ANIM_SELECTOR =
        runnerbursterEntity -> runnerbursterEntity.animationDispatcher.sendChomp();

    public static final AnimationSelector<ChestbursterEntity> NBUSTER_ANIM_SELECTOR = neobursterEntity -> {
        Runnable animKey = switch (neobursterEntity.getRandom().nextInt(4)) {
            case 1 -> neobursterEntity.animationDispatcher::sendRightClaw;
            case 2 -> neobursterEntity.animationDispatcher::sendLeftTail;
            case 3 -> neobursterEntity.animationDispatcher::sendRightTail;
            default -> neobursterEntity.animationDispatcher::sendLeftClaw;
        };
        GigCommonMethods.setAnimation(animKey);
    };

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

    public static final AnimationSelector<SpitterEntity> SPITTER_RANGE_SELECTOR = spitterEntity -> spitterEntity.animationDispatcher
        .sendAcidSpit();
}
