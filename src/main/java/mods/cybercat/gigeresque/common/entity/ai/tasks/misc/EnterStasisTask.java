package mods.cybercat.gigeresque.common.entity.ai.tasks.misc;

import com.mojang.datafixers.util.Pair;
import mod.azure.azurelib.common.api.common.animatable.GeoEntity;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.interfacing.AbstractAlien;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.DelayedBehaviour;

import java.util.List;

public class EnterStasisTask<E extends PathfinderMob & AbstractAlien & GeoEntity> extends DelayedBehaviour<E> {

    public EnterStasisTask(int delayTicks) {
        super(delayTicks);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of();
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        return entity.getDeltaMovement().horizontalDistance() == 0 && !entity.isVehicle() && entity.isAlive() && !entity.isSearching() && !entity.isHissing() && !entity.isPassedOut();
    }

    @Override
    protected void doDelayedAction(E entity) {
        entity.triggerAnim(Constants.ATTACK_CONTROLLER, "passout");
        entity.setPassedOutStatus(true);
    }
}
