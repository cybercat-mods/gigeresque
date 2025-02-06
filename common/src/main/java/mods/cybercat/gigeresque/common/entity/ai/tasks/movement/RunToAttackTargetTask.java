package mods.cybercat.gigeresque.common.entity.ai.tasks.movement;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.sblforked.api.core.behaviour.ExtendedBehaviour;
import mod.azure.azurelib.sblforked.util.BrainUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.ToIntBiFunction;

public class RunToAttackTargetTask<E extends Mob> extends ExtendedBehaviour<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS;

    protected BiFunction<E, LivingEntity, Float> speedMod = (owner, target) -> 1.0F;

    protected ToIntBiFunction<E, LivingEntity> closeEnoughWhen = (owner, target) -> 0;

    public RunToAttackTargetTask() {}

    public RunToAttackTargetTask<E> speedMod(BiFunction<E, LivingEntity, Float> speedModifier) {
        this.speedMod = speedModifier;
        return this;
    }

    public RunToAttackTargetTask<E> closeEnoughDist(ToIntBiFunction<E, LivingEntity> closeEnoughMod) {
        this.closeEnoughWhen = closeEnoughMod;
        return this;
    }

    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    protected void start(E entity) {
        Brain<?> brain = entity.getBrain();
        LivingEntity target = BrainUtils.getTargetOfEntity(entity);
        BrainUtils.setMemory(brain, MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
        BrainUtils.setMemory(
            brain,
            MemoryModuleType.WALK_TARGET,
            new WalkTarget(
                new EntityTracker(target, false),
                this.speedMod.apply(entity, target),
                this.closeEnoughWhen.applyAsInt(entity, target)
            )
        );
    }

    static {
        MEMORY_REQUIREMENTS = ObjectArrayList.of(
            new Pair[] {
                Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED),
                Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED),
                Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT) }
        );
    }
}
