package com.bvanseg.gigeresque.common.entity.ai.brain.sensor;

import com.bvanseg.gigeresque.common.block.tag.BlockTags;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;
import java.util.Set;

public class AlienRepellentSensor extends Sensor<LivingEntity> {
    private static Optional<BlockPos> findAlienRepellent(ServerWorld world, LivingEntity entity) {
        return BlockPos.findClosest(entity.getBlockPos(), 8, 4, pos -> isAlienRepellent(world, pos));
    }

    private static boolean isAlienRepellent(ServerWorld world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        return blockState.isIn(BlockTags.ALIEN_REPELLENTS);
    }

    @Override
    public Set<MemoryModuleType<?>> getOutputMemoryModules() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_REPELLENT);
    }

    @Override
    protected void sense(ServerWorld world, LivingEntity entity) {
        Brain<?> brain = entity.getBrain();
        brain.remember(MemoryModuleType.NEAREST_REPELLENT, findAlienRepellent(world, entity));
    }
}
