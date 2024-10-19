package mods.cybercat.gigeresque.common.entity.ai.tasks.attack;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.ToIntFunction;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.tasks.CustomDelayedRangedBehaviour;
import mods.cybercat.gigeresque.common.entity.impl.extra.SpitterEntity;
import mods.cybercat.gigeresque.interfacing.AnimationSelector;

public class AlienProjectileAttack<E extends AlienEntity> extends CustomDelayedRangedBehaviour<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
        Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT),
        Pair.of(MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT)
    );

    protected ToIntFunction<E> attackIntervalSupplier = entity -> 90;

    @Nullable
    protected LivingEntity target = null;

    public AlienProjectileAttack(int delayTicks, AnimationSelector<? super E> animationSelector) {
        super(delayTicks, animationSelector);
    }

    /**
     * Set the time between attacks.
     *
     * @param supplier The tick value provider
     * @return this
     */
    public AlienProjectileAttack<E> attackInterval(ToIntFunction<E> supplier) {
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

        return entity.getSensing().hasLineOfSight(this.target) && !entity.isWithinMeleeAttackRange(this.target);
    }

    @Override
    protected void start(E entity) {
        BehaviorUtils.lookAtEntity(entity, this.target);
        entity.setAttackingState(0);
    }

    @Override
    protected void stop(E entity) {
        this.target = null;
        entity.setAttackingState(0);
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

        if (!entity.getSensing().hasLineOfSight(this.target) || entity.isWithinMeleeAttackRange(this.target))
            return;

        if (entity instanceof SpitterEntity spitterEntity) {
            entity.swing(InteractionHand.MAIN_HAND);
            spitterEntity.shootAcid(this.target, entity);
        }
    }

}
