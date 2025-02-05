package mods.cybercat.gigeresque.common.entity.helper.managers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.nav.GigNavigation;

public class StasisManager {

    private static final String STASIS_TAG_KEY = "isStasis";

    private final AlienEntity entity;

    private final EntityDataAccessor<Boolean> isStasisEDA;

    public StasisManager(AlienEntity entity, EntityDataAccessor<Boolean> isStasisEDA) {
        this.entity = entity;
        this.isStasisEDA = isStasisEDA;
    }

    public void tick() {
        if (entity.level().isClientSide) {
            return;
        }

        if (entity.isAggressive() || entity.isExecuting()) {
            setStasis(false);
        }
//        if (entity.stasisManager.isStasis() && entity.getNavigation() instanceof GigNavigation gigNav)
//            gigNav.stop();

        if (isStasis() && entity.isAggressive()) {
            entity.animationDispatcher.sendStatisLeave();
            setStasis(false);
        }
    }

    public void setStasis(boolean searching) {
        entity.getEntityData().set(isStasisEDA, searching);
    }

    public boolean isStasis() {
        return entity.getEntityData().get(isStasisEDA);
    }

    public void load(CompoundTag compoundTag) {
        entity.getEntityData().set(isStasisEDA, compoundTag.getBoolean(STASIS_TAG_KEY));
    }

    public void save(CompoundTag compoundTag) {
        compoundTag.putBoolean(STASIS_TAG_KEY, entity.getEntityData().get(isStasisEDA));
    }
}
