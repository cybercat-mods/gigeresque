package mods.cybercat.gigeresque.common.entity.ai.tasks.misc;

import com.mojang.datafixers.util.Pair;
import mod.azure.azurelib.sblforked.api.core.behaviour.DelayedBehaviour;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.List;

import mods.cybercat.gigeresque.interfacing.AbstractAlien;

public class EnterStasisTask<E extends PathfinderMob & AbstractAlien> extends DelayedBehaviour<E> {

    public EnterStasisTask(int delayTicks) {
        super(delayTicks);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of();
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        return entity.getDeltaMovement().horizontalDistance() == 0 && !entity.isVehicle() && entity.isAlive() && !entity.isSearching()
            && !entity.isHissing() && !entity.isPassedOut();
    }

    @Override
    protected void doDelayedAction(E entity) {
        entity.setPassedOutStatus(true);
    }
}
