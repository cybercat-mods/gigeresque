package mods.cybercat.gigeresque.common.entity.ai.tasks.movement;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.GigMemoryTypes;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import net.tslat.smartbrainlib.util.RandomUtil;

import java.util.List;
import java.util.Objects;

public class EggmorpthTargetTask<E extends AlienEntity> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(Pair.of(GigMemoryTypes.NEARBY_NEST_BLOCKS.get(), MemoryStatus.VALUE_PRESENT));

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void start(E entity) {
        entity.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, E entity, long gameTime) {
        return !entity.isFleeing();
    }

    @Override
    protected void tick(ServerLevel level, E entity, long gameTime) {
        if (entity.isCrawling() ||  entity.isTunnelCrawling())
            return;
        var lightSourceLocation = entity.getBrain().getMemory(GigMemoryTypes.NEARBY_NEST_BLOCKS.get()).orElse(null);
        var target = entity.getFirstPassenger();
        if (lightSourceLocation == null || !lightSourceLocation.stream().findAny().isPresent())
            return;
        var test = RandomUtil.getRandomPositionWithinRange(entity.blockPosition(), 3, 1, 3, false, level);
        var nestLocation = lightSourceLocation.stream().findAny().get().getFirst();

        if (target != null && test != nestLocation)
            if (!nestLocation.closerToCenterThan(entity.position(), 1.4)) {
                BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(nestLocation, 2.5F, 1));
            } else {
                for (BlockPos testPos : BlockPos.betweenClosed(test, test.above(2)))
                    if (level.getBlockState(test).isAir() && level.getBlockState(test.below()).isSolid() && level.getEntitiesOfClass(LivingEntity.class, new AABB(test)).stream().noneMatch(Objects::isNull)) {
                        BrainUtils.clearMemory(entity, MemoryModuleType.WALK_TARGET);
                        ((Eggmorphable) target).setTicksUntilEggmorphed(Gigeresque.config.getEggmorphTickTimer());
                        target.setPos(Vec3.atBottomCenterOf(testPos));
                        target.removeVehicle();
                        entity.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
                        level.setBlockAndUpdate(testPos, GigBlocks.NEST_RESIN_WEB_CROSS.defaultBlockState());
                        level.setBlockAndUpdate(testPos.above(), GigBlocks.NEST_RESIN_WEB_CROSS.defaultBlockState());
                    }
            }
    }

}
