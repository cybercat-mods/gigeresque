package mods.cybercat.gigeresque.common.entity.ai.tasks.movement;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.ai.tasks.CustomDelayedMeleeBehaviour;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.Items;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.ToIntFunction;

public class FacehuggerPounceTask<E extends FacehuggerEntity> extends CustomDelayedMeleeBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT),
            Pair.of(MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT));
    protected ToIntFunction<E> attackIntervalSupplier = entity -> 80;

    @Nullable
    protected LivingEntity target = null;

    public FacehuggerPounceTask(int delayTicks) {
        super(delayTicks);
    }

    /**
     * Set the time between attacks.
     *
     * @param supplier The tick value provider
     * @return this
     */
    public FacehuggerPounceTask<E> attackInterval(ToIntFunction<E> supplier) {
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
        return GigEntityUtils.faceHuggerTest(this.target) && !entity.hasEffect(
                MobEffects.MOVEMENT_SLOWDOWN) && entity.isWithinMeleeAttackRange(
                this.target) && this.target.level().getBlockStates(this.target.getBoundingBox().inflate(1)).noneMatch(
                state -> state.is(GigBlocks.NEST_RESIN_WEB_CROSS)) && entity.getMobType() != MobType.UNDEAD;
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
        BrainUtils.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true,
                this.attackIntervalSupplier.applyAsInt(entity));

        if (this.target == null)
            return;

        if (this.target.level().getBlockStates(this.target.getBoundingBox().inflate(1)).anyMatch(
                state -> state.is(GigBlocks.NEST_RESIN_WEB_CROSS)))
            return;

        if (entity.distanceTo(this.target) < 1.1F)
            return;

        if (!this.target.getUseItem().is(Items.SHIELD))
            entity.grabTarget(this.target);
    }
}
