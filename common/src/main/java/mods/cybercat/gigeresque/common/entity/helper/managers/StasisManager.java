package mods.cybercat.gigeresque.common.entity.helper.managers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.ai.goal.Goal;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.nav.GigNavigation;

public class StasisManager {

    private static final String STASIS_TAG_KEY = "isStasis";

    private static final String STASIS_TICK_KEY = "statisTicks";

    private final AlienEntity entity;

    private int stasisTicks = 0;

    private final EntityDataAccessor<Boolean> isStasisEDA;

    private final EntityDataAccessor<Integer> statisTickCounterEDA;

    public StasisManager(AlienEntity entity, EntityDataAccessor<Boolean> isStasisEDA, EntityDataAccessor<Integer> statisTickCounterEDA) {
        this.entity = entity;
        this.isStasisEDA = isStasisEDA;
        this.statisTickCounterEDA = statisTickCounterEDA;
    }

    public void tick() {
        if (entity.level().isClientSide) {
            return;
        }

        if (entity.isAggressive() || entity.isExecuting()) {
            stasisTicks = 0;
            setStasis(false);
        }
        if (entity.stasisManager.isStasis() && entity.getNavigation() instanceof GigNavigation gigNav)
            gigNav.stop();

        if (isStasis() && entity.isAggressive()) {
            entity.animationDispatcher.sendStatisLeave();
            stasisTicks = 0;
            setStasis(false);
        }
        if (!isStasis() && !entity.isAggressive() && !entity.isExecuting() && !entity.isVehicle()) {
            setStasisTickCounter(stasisTicks++);
        }

        if (!isStasis() && !entity.isAggressive() && !entity.isExecuting() && !entity.isVehicle() && stasisTicks > 6000) {
            setStasis(true);
            stasisTicks = 0;
        }
        if (isStasis()) {
            entity.goalSelector.disableControlFlag(Goal.Flag.JUMP);
            entity.goalSelector.disableControlFlag(Goal.Flag.LOOK);
            entity.goalSelector.disableControlFlag(Goal.Flag.MOVE);
            entity.goalSelector.disableControlFlag(Goal.Flag.TARGET);
        }
    }

    public void setStasis(boolean stasis) {
        entity.getEntityData().set(isStasisEDA, stasis);
        if (!stasis) {
            entity.goalSelector.enableControlFlag(Goal.Flag.JUMP);
            entity.goalSelector.enableControlFlag(Goal.Flag.LOOK);
            entity.goalSelector.enableControlFlag(Goal.Flag.MOVE);
            entity.goalSelector.enableControlFlag(Goal.Flag.TARGET);
        }
    }

    public boolean isStasis() {
        return entity.getEntityData().get(isStasisEDA);
    }

    public int getStasisTickCounter() {
        return entity.getEntityData().get(statisTickCounterEDA);
    }

    public void setStasisTickCounter(int counter) {
        entity.getEntityData().set(statisTickCounterEDA, counter);
    }

    public void load(CompoundTag compoundTag) {
        entity.getEntityData().set(isStasisEDA, compoundTag.getBoolean(STASIS_TAG_KEY));
        entity.getEntityData().set(statisTickCounterEDA, compoundTag.getInt(STASIS_TICK_KEY));
    }

    public void save(CompoundTag compoundTag) {
        compoundTag.putBoolean(STASIS_TAG_KEY, entity.getEntityData().get(isStasisEDA));
        compoundTag.putInt(STASIS_TICK_KEY, entity.getEntityData().get(statisTickCounterEDA));
    }
}
