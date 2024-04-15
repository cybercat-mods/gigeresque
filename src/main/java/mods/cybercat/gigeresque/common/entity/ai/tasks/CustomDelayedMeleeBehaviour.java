package mods.cybercat.gigeresque.common.entity.ai.tasks;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.aqua.AquaticAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.ClassicAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.entity.impl.extra.SpitterEntity;
import mods.cybercat.gigeresque.common.entity.impl.mutant.HammerpedeEntity;
import mods.cybercat.gigeresque.common.entity.impl.mutant.PopperEntity;
import mods.cybercat.gigeresque.common.entity.impl.mutant.StalkerEntity;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeomorphAdolescentEntity;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeomorphEntity;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerAlienEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.function.Consumer;

public abstract class CustomDelayedMeleeBehaviour<E extends AlienEntity> extends ExtendedBehaviour<E> {
    protected final int delayTime;
    protected long delayFinishedAt = 0;
    protected Consumer<E> delayedCallback = entity -> {
    };

    protected CustomDelayedMeleeBehaviour(int delayTicks) {
        this.delayTime = delayTicks;

        runFor(entity -> Math.max(delayTicks, 60));
    }

    /**
     * A callback for when the delayed action is called.
     *
     * @param callback The callback
     * @return this
     */
    public final CustomDelayedMeleeBehaviour<E> whenActivating(Consumer<E> callback) {
        this.delayedCallback = callback;

        return this;
    }

    @Override
    protected final void start(ServerLevel level, E entity, long gameTime) {
        if (this.delayTime > 0) {
            this.delayFinishedAt = gameTime + this.delayTime;
            super.start(level, entity, gameTime);
        } else {
            super.start(level, entity, gameTime);
            doDelayedAction(entity);
        }
        entity.setAttackingState(1);
        var cName = "attackController";
        var leftClaw = "left_claw";
        var rightClaw = "right_claw";
        var attackNormal = "attack_normal";
        if (entity instanceof ClassicAlienEntity classic) {
            boolean basicCheck = classic.isCrawling() || classic.isTunnelCrawling() || classic.isInWater();
            classic.triggerAnim(cName, switch (classic.getRandom().nextInt(4)) {
                case 0 -> basicCheck ? "left_claw_basic" : leftClaw;
                case 1 -> basicCheck ? "right_claw_basic" : rightClaw;
                case 2 -> basicCheck ? "left_tail_basic" : "left_tail";
                case 3 -> basicCheck ? "right_tail_basic" : "right_tail";
                default -> basicCheck ? "left_claw_basic" : leftClaw;
            });
        }
        if (entity instanceof AquaticAlienEntity || entity instanceof NeomorphEntity || entity instanceof NeomorphAdolescentEntity || entity instanceof SpitterEntity) {
            entity.triggerAnim(cName, switch (entity.getRandom().nextInt(4)) {
                case 0 -> leftClaw;
                case 1 -> rightClaw;
                case 2 -> "left_tail";
                case 3 -> "right_tail";
                default -> leftClaw;
            });
        }
        if (entity instanceof StalkerEntity stalker) {
            stalker.triggerAnim("livingController", switch (stalker.getRandom().nextInt(4)) {
                case 0 -> attackNormal;
                case 1 -> "attack_heavy";
                case 2 -> attackNormal;
                case 3 -> "attack_heavy";
                default -> attackNormal;
            });
        }
        if (entity instanceof RunnerAlienEntity runner) {
            runner.triggerAnim(cName, switch (runner.getRandom().nextInt(4)) {
                case 0 -> leftClaw;
                case 1 -> rightClaw;
                case 2 -> "left_tail_basic";
                case 3 -> "right_tail_basic";
                default -> leftClaw;
            });
        }

        if (entity instanceof HammerpedeEntity hammer) hammer.triggerAnim(cName, "attack");

        if (entity instanceof FacehuggerEntity hugger && hugger.getTarget() != null) {
            var vec3d2 = new Vec3(hugger.getTarget().getX() - hugger.getX(), 0.0,
                    hugger.getTarget().getZ() - hugger.getZ());
            vec3d2 = vec3d2.normalize().scale(0.2).add(hugger.getDeltaMovement().scale(0.2));
            hugger.setDeltaMovement(vec3d2.x, hugger.getTarget().getEyeHeight() > 0.8 ? 0.5F : 0.4, vec3d2.z);
            hugger.setJumping(true);
        }

        if (entity instanceof PopperEntity popper && popper.getTarget() != null) {
            var vec3d2 = new Vec3(popper.getTarget().getX() - popper.getX(), 0.0,
                    popper.getTarget().getZ() - popper.getZ());
            vec3d2 = vec3d2.normalize().scale(0.2).add(popper.getDeltaMovement().scale(0.2));
            popper.setDeltaMovement(vec3d2.x, 0.5F, vec3d2.z);
        }
    }

    @Override
    protected final void stop(ServerLevel level, E entity, long gameTime) {
        super.stop(level, entity, gameTime);

        this.delayFinishedAt = 0;
        entity.setAttackingState(0);
    }

    @Override
    protected boolean shouldKeepRunning(E entity) {
        return this.delayFinishedAt >= entity.level().getGameTime();
    }

    @Override
    protected final void tick(ServerLevel level, E entity, long gameTime) {
        super.tick(level, entity, gameTime);

        if (this.delayFinishedAt <= gameTime) {
            doDelayedAction(entity);
            this.delayedCallback.accept(entity);
        }
    }

    /**
     * The action to take once the delay period has elapsed.
     *
     * @param entity The owner of the brain
     */
    protected void doDelayedAction(E entity) {
    }
}
