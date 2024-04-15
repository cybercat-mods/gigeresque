package mods.cybercat.gigeresque.common.entity.ai.tasks;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mods.cybercat.gigeresque.common.entity.impl.AdultAlienEntity;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.nest.NestBuildingHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.tslat.smartbrainlib.api.core.behaviour.DelayedBehaviour;

import java.util.List;

public class BuildNestTask<E extends AdultAlienEntity> extends DelayedBehaviour<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED), Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT));

    public BuildNestTask(int delayTicks) {
        super(delayTicks);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E alien) {
        return !alien.isCrawling() && !alien.isAggressive() && !alien.isVehicle() && alien.getGrowth() == alien.getMaxGrowth() && !alien.level().canSeeSky(alien.blockPosition()) && !alien.level().getBlockState(alien.blockPosition()).is(Blocks.SOUL_SAND) && alien.level().getBrightness(LightLayer.SKY, alien.blockPosition()) <= 5;
    }

    @Override
    protected void doDelayedAction(E alien) {
        if (!alien.isCrawling() && !alien.getFeetBlockState().is(GigTags.NEST_BLOCKS) && !alien.level().canSeeSky(alien.blockPosition()) && alien.level().getBrightness(LightLayer.SKY, alien.blockPosition()) <= 5)
            NestBuildingHelper.tryBuildNestAround(alien.level(), alien.blockPosition());
    }
}
