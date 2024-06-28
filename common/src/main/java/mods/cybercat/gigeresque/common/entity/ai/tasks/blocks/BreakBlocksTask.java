package mods.cybercat.gigeresque.common.entity.ai.tasks.blocks;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.common.api.common.animatable.GeoEntity;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.particle.GigParticles;
import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.interfacing.AbstractAlien;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.material.Fluids;
import net.tslat.smartbrainlib.api.core.behaviour.DelayedBehaviour;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.ToIntFunction;

public class BreakBlocksTask<E extends PathfinderMob & AbstractAlien & GeoEntity> extends DelayedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(SBLMemoryTypes.NEARBY_BLOCKS.get(), MemoryStatus.VALUE_PRESENT));

    protected ToIntFunction<E> attackIntervalSupplier = entity -> 90;

    @Nullable
    protected LivingEntity target = null;

    protected boolean useAcidBlood;

    public BreakBlocksTask(int delayTicks, boolean useAcidBlood) {
        super(delayTicks);
        this.useAcidBlood = useAcidBlood;
    }

    /**
     * Set the time between attacks.
     *
     * @param supplier The tick value provider
     * @return this
     */
    public BreakBlocksTask<E> attackInterval(ToIntFunction<E> supplier) {
        this.attackIntervalSupplier = supplier;

        return this;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        var notinWater = !(entity.level().getFluidState(entity.blockPosition()).is(
                Fluids.WATER) && entity.level().getFluidState(entity.blockPosition()).getAmount() >= 8);
        var canGrief = entity.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
        return !entity.isCrawling() && !entity.isTunnelCrawling() && !entity.isDeadOrDying() && !entity.isPassedOut() && notinWater && canGrief;
    }

    @Override
    protected void doDelayedAction(E entity) {
        for (var testPos : BlockPos.betweenClosed(entity.blockPosition().relative(entity.getDirection()),
                entity.blockPosition().relative(entity.getDirection()).above(2))) {
            var state = entity.level().getBlockState(testPos);
            if (!state.isSolid()) {
                if (state.is(GigTags.WEAK_BLOCKS) && !state.isAir()) {
                    if (!entity.level().isClientSide) {
                        entity.level().destroyBlock(testPos, true, null, 512);
                    }
                    if (!entity.isVehicle()) {
                        entity.triggerAnim(Constants.ATTACK_CONTROLLER, "swipe");
                    }
                    if (entity.isVehicle()) {
                        entity.triggerAnim(Constants.ATTACK_CONTROLLER, "swipe_left_tail");
                    }
                    if (entity.level().isClientSide()) {
                        for (var i = 2; i < 10; i++) {
                            entity.level().addAlwaysVisibleParticle(GigParticles.ACID.get(),
                                    entity.getX() + ((entity.getRandom().nextDouble() / 2.0) - 0.5) * (entity.getRandom().nextBoolean() ? -1 : 1),
                                    entity.getEyeY() - ((entity.getEyeY() - entity.blockPosition().getY()) / 2.0),
                                    entity.getZ() + ((entity.getRandom().nextDouble() / 2.0) - 0.5) * (entity.getRandom().nextBoolean() ? -1 : 1),
                                    0.0, -0.15, 0.0);
                        }
                        entity.level().playLocalSound(testPos.getX(), testPos.getY(), testPos.getZ(),
                                SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS,
                                0.2f + entity.getRandom().nextFloat() * 0.2f,
                                0.9f + entity.getRandom().nextFloat() * 0.15f, false);
                    }
                } else if (!entity.isCrawling() && !entity.isTunnelCrawling() && !entity.isVehicle() && !state.is(
                        GigTags.ACID_RESISTANT) && !state.isAir() && (entity.getHealth() >= (entity.getMaxHealth() * 0.50)) && useAcidBlood) {
                    if (!entity.level().isClientSide) {
                        GigCommonMethods.generateGooBlood(entity, testPos.above(), 0, 0);
                    }
                    entity.hurt(entity.damageSources().generic(), 5);
                }
            }
        }
    }

}