package com.bvanseg.gigeresque.common.entity.ai.brain.task;

import com.bvanseg.gigeresque.common.entity.impl.AdultAlienEntityJava;
import com.bvanseg.gigeresque.common.util.nest.NestBuildingHelperJava;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.server.world.ServerWorld;

import static java.lang.Math.max;

public class BuildNestTaskJava extends Task<AdultAlienEntityJava> {
    private int cooldown = 0;
    public BuildNestTaskJava() {
        super(ImmutableMap.of(
                MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED,
                MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_ABSENT,
                MemoryModuleType.HOME, MemoryModuleState.VALUE_PRESENT
        ));
    }

    @Override
    public boolean shouldRun(ServerWorld serverWorld, AdultAlienEntityJava alien) {
        var homePos = alien.getBrain().getOptionalMemory(MemoryModuleType.HOME).orElse(null);
        if (homePos == null) return false;

        var isWithinNestRange = homePos.getPos().getManhattanDistance(alien.getBlockPos()) < 50;
        cooldown = max(cooldown - 1, 0);
        return alien.getGrowth() == alien.getMaxGrowth() &&
                cooldown <= 0 && isWithinNestRange && !alien.world.isSkyVisible(alien.getBlockPos());
    }

    @Override
    public void run( ServerWorld serverWorld, AdultAlienEntityJava alien, long l) {
        NestBuildingHelperJava.tryBuildNestAround(alien);
        cooldown += 180;
    }
}
