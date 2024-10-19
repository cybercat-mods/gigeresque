package mods.cybercat.gigeresque.common.entity.ai.tasks.attack;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.SplittableRandom;
import java.util.function.ToIntFunction;

import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.ai.tasks.CustomDelayedMeleeBehaviour;
import mods.cybercat.gigeresque.common.entity.helper.GigMeleeAttackSelector;
import mods.cybercat.gigeresque.common.entity.impl.mutant.PopperEntity;

public class AttackExplodeTask<E extends PopperEntity> extends CustomDelayedMeleeBehaviour<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
        Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT),
        Pair.of(MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT)
    );

    protected ToIntFunction<E> attackIntervalSupplier = entity -> 20;

    @Nullable
    protected LivingEntity target = null;

    public AttackExplodeTask(int delayTicks) {
        super(delayTicks, GigMeleeAttackSelector.POPPER_SELECTOR);
    }

    /**
     * Set the time between attacks.
     *
     * @param supplier The tick value provider
     * @return this
     */
    public AttackExplodeTask<E> attackInterval(ToIntFunction<E> supplier) {
        this.attackIntervalSupplier = supplier;

        return this;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        this.target = BrainUtils.getTargetOfEntity(entity);
        return entity.getSensing().hasLineOfSight(this.target) && entity.isWithinMeleeAttackRange(
            this.target
        ) && this.target.level()
            .getBlockStates(this.target.getBoundingBox().inflate(1))
            .noneMatch(
                state -> state.is(GigBlocks.NEST_RESIN_WEB_CROSS)
            );
    }

    @Override
    protected void start(E entity) {
        BehaviorUtils.lookAtEntity(entity, this.target);
    }

    @Override
    protected void stop(E entity) {
        this.target = null;
    }

    @Override
    protected void doDelayedAction(E entity) {
        BrainUtils.setForgettableMemory(
            entity,
            MemoryModuleType.ATTACK_COOLING_DOWN,
            true,
            this.attackIntervalSupplier.applyAsInt(entity)
        );

        if (this.target == null)
            return;

        if (!entity.getSensing().hasLineOfSight(this.target) || !entity.isWithinMeleeAttackRange(this.target))
            return;

        if (
            this.target.level()
                .getBlockStates(this.target.getBoundingBox().inflate(1))
                .anyMatch(
                    state -> state.is(GigBlocks.NEST_RESIN_WEB_CROSS)
                )
        )
            return;

        var random = new SplittableRandom();
        var randomPhase = random.nextInt(0, 100);
        if (randomPhase >= 90) {
            entity.explode();
            entity.remove(RemovalReason.KILLED);
        } else {
            entity.doHurtTarget(this.target);
            entity.swing(InteractionHand.MAIN_HAND);
        }
    }
}
