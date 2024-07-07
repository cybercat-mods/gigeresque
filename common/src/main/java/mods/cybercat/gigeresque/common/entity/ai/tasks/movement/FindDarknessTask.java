package mods.cybercat.gigeresque.common.entity.ai.tasks.movement;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.common.api.common.animatable.GeoEntity;
import mod.azure.azurelib.sblforked.api.core.behaviour.ExtendedBehaviour;
import mod.azure.azurelib.sblforked.util.BrainUtils;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.interfacing.AbstractAlien;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

public class FindDarknessTask<E extends PathfinderMob & AbstractAlien & GeoEntity> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT),
            Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED));

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean doStartCheck(ServerLevel level, E entity, long gameTime) {
        return level.isDay() && !entity.isAggressive() && !isInDarkness(level, entity);
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull E entity, long gameTime) {
        BlockPos targetPos = findDarkArea(level, entity);
        if (targetPos != null) {
            BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(targetPos, 2.5F, 0));
        }
    }

    private boolean isInDarkness(ServerLevel level, E entity) {
        return level.getMaxLocalRawBrightness(new BlockPos((int) entity.getX(), (int) entity.getEyeY(),
                (int) entity.getZ())) < 8;
    }

    private BlockPos findDarkArea(ServerLevel level, E entity) {
        for (var i = 0; i < 10; i++) {
            var x = (int) (entity.getX() + entity.getRandom().nextInt(20) - 10);
            var z = (int) (entity.getZ() + entity.getRandom().nextInt(20) - 10);
            var y = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
            var pos = new BlockPos(x, y, z);
            if (level.getBlockState(pos).getBlock() == Blocks.AIR && level.getMaxLocalRawBrightness(pos) < 8) {
                return pos;
            }
        }
        return null;
    }
}
