package com.bvanseg.gigeresque.common.entity.ai.brain.sensor;

import com.bvanseg.gigeresque.common.block.tag.BlockTagsJava;
import com.bvanseg.gigeresque.common.entity.AlienEntityJava;
import com.bvanseg.gigeresque.common.entity.ai.brain.memory.MemoryModuleTypesJava;
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

public class DestructibleLightSensorJava<E extends LivingEntity> extends Sensor<E> {
    private Optional<BlockPos> findDestructibleLight(ServerWorld world, E alien) {
        return BlockPos.findClosest(alien.getBlockPos(), 8, 3, pos -> isDestructibleLight(world, pos));
    }

    private static boolean isDestructibleLight(ServerWorld world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        return blockState.isIn(BlockTagsJava.DESTRUCTIBLE_LIGHT);
    }

    @Override
    public Set<MemoryModuleType<?>> getOutputMemoryModules() {
        return ImmutableSet.of(
                MemoryModuleTypesJava.NEAREST_LIGHT_SOURCE
        );
    }

    @Override
    protected void sense(ServerWorld world, E alien) {
        Brain<?> brain = alien.getBrain();
        brain.remember(MemoryModuleTypesJava.NEAREST_LIGHT_SOURCE, findDestructibleLight(world, alien));
    }
}
