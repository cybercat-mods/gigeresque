package mods.cybercat.gigeresque.common.entity.ai.tasks.misc;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.sblforked.api.core.behaviour.DelayedBehaviour;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

import java.util.List;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.nest.NestBuildingHelper;

public class BuildNestTask<E extends AlienEntity> extends DelayedBehaviour<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
        Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED),
        Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT)
    );

    public BuildNestTask(int delayTicks) {
        super(delayTicks);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E alien) {
        if (
            alien.level()
                .getBlockStatesIfLoaded(alien.getBoundingBox().inflate(10))
                .anyMatch(blockState -> blockState.is(GigTags.DUNGEON_BLOCKS) || blockState.is(GigTags.DUNGEON_STAIRS))
        )
            return false;
        return !alien.isCrawling() && !alien.isTunnelCrawling() && !alien.isAggressive() && !alien.isVehicle() && alien.getGrowth() == alien
            .getMaxGrowth() && !alien.level()
                .canSeeSky(
                    alien.blockPosition()
                ) && !alien.level()
                    .getBlockState(alien.blockPosition())
                    .is(
                        Blocks.SOUL_SAND
                    ) && alien.level().getBrightness(LightLayer.SKY, alien.blockPosition()) <= 5;
    }

    @Override
    protected void doDelayedAction(E alien) {
        final var dungeonBlockCheck = alien.level()
            .getBlockStates(new AABB(alien.blockPosition()).inflate(8D))
            .anyMatch(blockState -> blockState.is(GigTags.DUNGEON_BLOCKS));
        if (
            !dungeonBlockCheck && !alien.isCrawling() && !alien.isTunnelCrawling() && !alien.getInBlockState()
                .is(
                    GigTags.NEST_BLOCKS
                ) && !alien.level().canSeeSky(alien.blockPosition()) && alien.level()
                    .getBrightness(
                        LightLayer.SKY,
                        alien.blockPosition()
                    ) <= 5
        )
            NestBuildingHelper.tryBuildNestAround(alien.level(), alien.blockPosition(), alien);
    }
}
