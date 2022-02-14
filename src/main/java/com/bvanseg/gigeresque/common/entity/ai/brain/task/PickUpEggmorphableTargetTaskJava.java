package com.bvanseg.gigeresque.common.entity.ai.brain.task;

import com.bvanseg.gigeresque.common.entity.ai.brain.memory.MemoryModuleTypesJava;
import com.bvanseg.gigeresque.common.entity.impl.AdultAlienEntityJava;
import com.bvanseg.gigeresque.common.util.EntityUtils;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.server.world.ServerWorld;

public class PickUpEggmorphableTargetTaskJava extends Task<AdultAlienEntityJava> {
    private final double speed;
    public PickUpEggmorphableTargetTaskJava(double speed) {
        super(ImmutableMap.of(
                MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED,
                MemoryModuleTypesJava.EGGMORPH_TARGET, MemoryModuleState.VALUE_PRESENT,
                MemoryModuleType.HOME, MemoryModuleState.VALUE_PRESENT
        ));
        this.speed = speed;
    }

    @Override
    protected boolean shouldRun(ServerWorld world, AdultAlienEntityJava alien) {
        if (alien.getBrain().hasMemoryModule(MemoryModuleTypesJava.EGGMORPH_TARGET)) {
            var target = alien.getBrain().getOptionalMemory(MemoryModuleTypesJava.EGGMORPH_TARGET).orElse(null);
            if (target == null) return false;
            if (EntityUtils.isEggmorphable(target) && !EntityUtils.isFacehuggerAttached(target)) {
                return !alien.isCarryingEggmorphableTarget();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    protected void run(ServerWorld serverWorld, AdultAlienEntityJava alien, long l) {
        var target = alien.getBrain().getOptionalMemory(MemoryModuleTypesJava.EGGMORPH_TARGET).orElse(null);
        if (target == null) return;

        if (alien.distanceTo(target) < 4.0) {
            if (target.getVehicle() == null) {
                target.startRiding(alien);
            }
        } else {
            alien.getNavigation().startMovingTo(target.getX(), target.getY(), target.getZ(), speed);
        }
    }
}
