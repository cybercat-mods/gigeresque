package mods.cybercat.gigeresque.common.entity.ai.tasks.misc;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.common.api.common.animatable.GeoEntity;
import mod.azure.azurelib.sblforked.api.core.behaviour.DelayedBehaviour;
import mods.cybercat.gigeresque.common.entity.helper.Growable;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.nest.NestBuildingHelper;
import mods.cybercat.gigeresque.interfacing.AbstractAlien;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class BuildNestTask<E extends PathfinderMob & AbstractAlien & GeoEntity & Growable> extends DelayedBehaviour<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED),
            Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT));

    public BuildNestTask(int delayTicks) {
        super(delayTicks);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E alien) {
        return !alien.isCrawling() && !alien.isTunnelCrawling() && !alien.isAggressive() && !alien.isVehicle() && alien.getGrowth() == alien.getMaxGrowth() && !alien.level().canSeeSky(
                alien.blockPosition()) && !alien.level().getBlockState(alien.blockPosition()).is(
                Blocks.SOUL_SAND) && alien.level().getBrightness(LightLayer.SKY, alien.blockPosition()) <= 5;
    }

    @Override
    protected void doDelayedAction(E alien) {
        if (!alien.isCrawling() && !alien.isTunnelCrawling() && !alien.getInBlockState().is(
                GigTags.NEST_BLOCKS) && !alien.level().canSeeSky(alien.blockPosition()) && alien.level().getBrightness(
                LightLayer.SKY, alien.blockPosition()) <= 5)
            NestBuildingHelper.tryBuildNestAround(alien.level(), alien.blockPosition());
    }
}
