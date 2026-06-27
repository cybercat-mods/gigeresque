package mods.cybercat.gigeresque.common.entity.ai.goals.attack;

import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.source.GigDamageSources;

public class HeadBiteGoal extends Goal {

    protected final AlienEntity mob;

    private int executionTimer;

    public HeadBiteGoal(AlienEntity mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.mob.isVehicle() && this.mob.allowExecution;
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.isVehicle() && this.mob.allowExecution;
    }

    @Override
    public void start() {
        this.executionTimer = 0;
    }

    @Override
    public void stop() {
        this.mob.setIsBiting(false);
        this.mob.setIsExecuting(false);
        this.executionTimer = 0;
    }

    @Override
    public void tick() {
        if (!this.mob.level().isClientSide) {
            this.executionTimer++;
        }

        if (this.executionTimer >= 540) {
            this.mob.getNavigation().stop();
            this.mob.setSpeed(0.0F);
        }

        if (this.executionTimer == 560) {
            this.mob.animationDispatcher.sendExecution();
            this.mob.setIsExecuting(true);
        }

        if (this.executionTimer == 600) {
            this.mob.heal(50);
            if (this.mob.getFirstPassenger() != null) {
                this.mob.getFirstPassenger()
                    .hurt(
                        GigDamageSources.of(this.mob.level(), GigDamageSources.EXECUTION),
                        Integer.MAX_VALUE
                    );
            }
            this.mob.setIsBiting(false);
            this.mob.setIsExecuting(false);
            this.mob.allowExecution = false;
            this.executionTimer = 0;
        }
    }
}
