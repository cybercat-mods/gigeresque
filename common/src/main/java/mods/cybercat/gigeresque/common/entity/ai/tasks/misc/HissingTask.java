package mods.cybercat.gigeresque.common.entity.ai.tasks.misc;

import com.mojang.datafixers.util.Pair;
import mod.azure.azurelib.common.api.common.animatable.GeoEntity;
import mod.azure.azurelib.sblforked.api.core.behaviour.DelayedBehaviour;
import mods.cybercat.gigeresque.interfacing.AbstractAlien;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HissingTask<E extends PathfinderMob & AbstractAlien & GeoEntity> extends DelayedBehaviour<E> {

    @Nullable
    protected LivingEntity target = null;

    public HissingTask(int delayTicks) {
        super(delayTicks);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of();
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        return entity.getDeltaMovement().horizontalDistance() == 0 && !entity.isInWater() && !entity.level().isClientSide && (!entity.isSearching() && !entity.isVehicle() && entity.isAlive() && !entity.isPassedOut()) && !entity.isAggressive() && !entity.isTunnelCrawling() && !entity.isCrawling();
    }

    @Override
    protected void start(E entity) {
        super.start(entity);
    }

    @Override
    protected void stop(E entity) {
        entity.setIsHissing(false);
        super.stop(entity);
    }

    @Override
    protected void doDelayedAction(E entity) {
        entity.triggerAnim("hissController", "hiss");
        entity.setIsHissing(true);
    }
}
