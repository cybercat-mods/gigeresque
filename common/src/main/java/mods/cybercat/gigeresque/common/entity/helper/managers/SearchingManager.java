package mods.cybercat.gigeresque.common.entity.helper.managers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;

import mods.cybercat.gigeresque.common.entity.AlienEntity;

public class SearchingManager {

    private static final String SEARCHING_TAG_KEY = "searching";

    private final AlienEntity entity;

    private final EntityDataAccessor<Boolean> isSearchingEDA;

    public SearchingManager(AlienEntity entity, EntityDataAccessor<Boolean> isSearchingEDA) {
        this.entity = entity;
        this.isSearchingEDA = isSearchingEDA;
    }

    public void tick() {
        if (entity.level().isClientSide) {
            return;
        }

         if (isSearching() && !entity.moveAnalysis.isMoving() && entity.wakeupCounter >= 3) {
         setSearching(false);
         }
    }

    public void setSearching(boolean searching) {
        entity.getEntityData().set(isSearchingEDA, searching);
    }

    public boolean isSearching() {
        return entity.getEntityData().get(isSearchingEDA);
    }

    public void load(CompoundTag compoundTag) {
        entity.getEntityData().set(isSearchingEDA, compoundTag.getBoolean(SEARCHING_TAG_KEY));
    }

    public void save(CompoundTag compoundTag) {
        compoundTag.putBoolean(SEARCHING_TAG_KEY, entity.getEntityData().get(isSearchingEDA));
    }
}
