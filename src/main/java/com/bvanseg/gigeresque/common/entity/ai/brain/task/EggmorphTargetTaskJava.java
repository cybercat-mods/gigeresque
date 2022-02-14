package com.bvanseg.gigeresque.common.entity.ai.brain.task;

import com.bvanseg.gigeresque.ConstantsJava;
import com.bvanseg.gigeresque.common.block.BlocksJava;
import com.bvanseg.gigeresque.common.entity.ai.brain.memory.MemoryModuleTypesJava;
import com.bvanseg.gigeresque.common.entity.impl.AdultAlienEntityJava;
import com.bvanseg.gigeresque.interfacing.Eggmorphable;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class EggmorphTargetTaskJava extends Task<AdultAlienEntityJava> {
    private final double speed;
    private int timeRunning = 0;
    public EggmorphTargetTaskJava(double speed) {
        super(ImmutableMap.of(
                MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED,
                MemoryModuleTypesJava.NEAREST_ALIEN_WEBBING, MemoryModuleState.REGISTERED,
                MemoryModuleType.HOME, MemoryModuleState.VALUE_PRESENT
        ));
        this.speed = speed;
    }

    @Override
    protected boolean shouldRun(ServerWorld serverWorld, AdultAlienEntityJava alien) {
        return alien.isCarryingEggmorphableTarget();
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld serverWorld, AdultAlienEntityJava alien, long l) {
        return alien.isCarryingEggmorphableTarget() && (timeRunning++ < ConstantsJava.TPS * 10);
    }

    @Override
    protected void run(ServerWorld serverWorld, AdultAlienEntityJava alien, long l) {
        var target = alien.getFirstPassenger();
        if (target == null) return;
        var homeMemory = alien.getBrain().getOptionalMemory(MemoryModuleType.HOME).orElse(null);
        if (homeMemory == null) {
            return;
        }
        var home = homeMemory.getPos();

        if (alien.getBlockPos().getManhattanDistance(home) < 32 &&
                alien.getBrain().hasMemoryModule(MemoryModuleTypesJava.NEAREST_ALIEN_WEBBING)
        ) {
            var nearestWebbing = alien.getBrain().getOptionalMemory(MemoryModuleTypesJava.NEAREST_ALIEN_WEBBING).orElse(null);
            if (nearestWebbing == null) return;

            if (alien.getBlockPos().getManhattanDistance(nearestWebbing) < 4) {
                ((Eggmorphable) target).setTicksUntilEggmorphed(ConstantsJava.EGGMORPH_DURATION);
                target.stopRiding();
                target.setPosition(Vec3d.ofBottomCenter(nearestWebbing));

                var hasCeiling = false;

                var ceilingUp = nearestWebbing.up();
                for (int i = 0; i < 20; i++) {
                    if (alien.world.getBlockState(ceilingUp).isOpaqueFullCube(alien.world, ceilingUp)) {
                        hasCeiling = true;
                        break;
                    }
                    ceilingUp = ceilingUp.up();
                }

                var up = nearestWebbing.up();
                if (hasCeiling) {
                    for (int i = 0; i < 20; i++) {
                        var state = alien.world.getBlockState(up);

                        if (state.isAir() || state.getBlock() == BlocksJava.NEST_RESIN_WEB) {
                            alien.world.setBlockState(up, BlocksJava.NEST_RESIN_WEB_CROSS.getDefaultState());
                        } else {
                            break;
                        }
                        up = up.up();
                    }
                }

            } else {
                alien.getNavigation().startMovingTo(
                        nearestWebbing.getX(),
                        nearestWebbing.getY(),
                        nearestWebbing.getZ(),
                        speed
                );
            }

        } else {
            alien.getNavigation().startMovingTo(home.getX(), home.getY(), home.getZ(), speed);
        }
    }
}
