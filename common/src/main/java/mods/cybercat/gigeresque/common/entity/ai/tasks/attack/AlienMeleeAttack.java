package mods.cybercat.gigeresque.common.entity.ai.tasks.attack;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.common.api.common.animatable.GeoEntity;
import mod.azure.azurelib.sblforked.util.BrainUtils;
import mods.cybercat.gigeresque.common.entity.ai.tasks.CustomDelayedMeleeBehaviour;
import mods.cybercat.gigeresque.common.entity.impl.mutant.StalkerEntity;
import mods.cybercat.gigeresque.interfacing.AbstractAlien;
import mods.cybercat.gigeresque.interfacing.AnimationSelector;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.ToIntFunction;

public class AlienMeleeAttack<E extends PathfinderMob & AbstractAlien & GeoEntity> extends CustomDelayedMeleeBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), Pair.of(MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT));

    protected ToIntFunction<E> attackIntervalSupplier = entity -> 20;

    @Nullable
    protected LivingEntity target = null;

    public AlienMeleeAttack(int delayTicks, AnimationSelector<E> animationSelector) {
        super(delayTicks, animationSelector);
    }

    /**
     * Set the time between attacks.
     *
     * @param supplier The tick value provider
     * @return this
     */
    public AlienMeleeAttack<E> attackInterval(ToIntFunction<E> supplier) {
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

        assert this.target != null;
        return entity.getSensing().hasLineOfSight(this.target) && entity.isWithinMeleeAttackRange(this.target);
    }

    @Override
    protected void start(E entity) {
        assert this.target != null;
        BehaviorUtils.lookAtEntity(entity, this.target);
    }

    @Override
    protected void stop(E entity) {
        this.target = null;
    }

    @Override
    protected void doDelayedAction(E entity) {
        BrainUtils.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true, this.attackIntervalSupplier.applyAsInt(entity));

        if (this.target == null) return;

        if (!entity.getSensing().hasLineOfSight(this.target) || !entity.isWithinMeleeAttackRange(this.target)) return;

        if (entity instanceof StalkerEntity) {
            this.target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1, true, true));
            this.target.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 200, 1, true, true));
        }

        if (entity.isInWater() && !target.isInWater()) {
            Vec3 vec3 = entity.getDeltaMovement();
            Vec3 vec32 = new Vec3(this.target.getX() - entity.getX(), 0.0, this.target.getZ() - entity.getZ());
            if (vec32.lengthSqr() > 1.0E-7)
                vec32 = vec32.normalize().scale(1.4).add(vec3.scale(0.2));
            entity.setDeltaMovement(vec32.x, 1.3F, vec32.z);
        }

        entity.swing(InteractionHand.MAIN_HAND);
        entity.doHurtTarget(this.target);
    }
}