package com.bvanseg.gigeresque.common.entity.ai.brain.task;

import com.bvanseg.gigeresque.common.entity.AlienEntity;
import com.bvanseg.gigeresque.common.entity.ai.brain.memory.MemoryModuleTypes;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.LightType;

public class DestroyLightTask extends Task<AlienEntity> {
    private final double speed;

    public DestroyLightTask(double speed) {
        super(ImmutableMap.of(
                MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED,
                MemoryModuleTypes.NEAREST_LIGHT_SOURCE, MemoryModuleState.VALUE_PRESENT
        ));
        this.speed = speed;
    }

    @Override
    protected boolean shouldRun(ServerWorld serverWorld, AlienEntity alien) {
        return alien.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) &&
                alien.world.getLightLevel(LightType.BLOCK, alien.getBlockPos()) != 0;
    }

    @Override
    protected void run(ServerWorld serverWorld, AlienEntity alien, long l) {
        var lightSourceLocation = alien.getBrain().getOptionalMemory(MemoryModuleTypes.NEAREST_LIGHT_SOURCE).orElse(null);
        if (lightSourceLocation == null) return;
        startMovingToTarget(alien, lightSourceLocation);

        if (lightSourceLocation.isWithinDistance(alien.getPos(), getDesiredDistanceToTarget())) {
            var world = alien.world;
            var random = alien.getRandom();
            alien.world.removeBlock(lightSourceLocation, false);
            if (!world.isClient) {
                for (int i = 0; i < 20; i++) {
                    var e = random.nextGaussian() * 0.02;
                    var f = random.nextGaussian() * 0.02;
                    var g = random.nextGaussian() * 0.02;
                    ((ServerWorld) world).spawnParticles(
                            ParticleTypes.POOF,
                            ((double)lightSourceLocation.getX()) + 0.5,
                            lightSourceLocation.getY(),
                            ((double)lightSourceLocation.getZ()) + 0.5,
                            1,
                            e,
                            f,
                            g,
                            0.15000000596046448
                    );
                }
                alien.getBrain().forget(MemoryModuleTypes.NEAREST_LIGHT_SOURCE);
            }
        }
    }

    private double getDesiredDistanceToTarget() {
        return 3.14;
    }

    private void startMovingToTarget(AlienEntity alien, BlockPos targetPos) {
        alien.getNavigation().startMovingTo(
                ((double) ((float) targetPos.getX())) + 0.5,
                targetPos.getY(),
                ((double) ((float) targetPos.getZ())) + 0.5,
                speed
        );
    }
}
