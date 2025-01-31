package mods.cybercat.gigeresque.common.entity.ai.tasks.attack;

import com.mojang.datafixers.util.Pair;
import mod.azure.azurelib.common.internal.common.AzureLib;
import mod.azure.azurelib.common.platform.Services;
import mod.azure.azurelib.sblforked.api.core.behaviour.DelayedBehaviour;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.List;

import mods.cybercat.gigeresque.client.particle.GigParticles;
import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.impl.classic.ClassicAlienEntity;
import mods.cybercat.gigeresque.common.source.GigDamageSources;
import mods.cybercat.gigeresque.interfacing.AbstractAlien;

public class AlienHeadBiteTask<E extends PathfinderMob & AbstractAlien> extends DelayedBehaviour<E> {

    private long lastUpdateTime = 0L;

    public AlienHeadBiteTask(int delayTicks) {
        super(delayTicks);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of();
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        return entity.isVehicle();
    }

    @Override
    protected void tick(E entity) {
        if (entity.getFirstPassenger() != null) {
            var yOffset = entity.getEyeY() - ((entity.getFirstPassenger().getEyeY() - entity.getFirstPassenger().blockPosition().getY())
                / 2.0);
            var e = entity.getFirstPassenger().getX() + ((entity.getRandom().nextDouble() / 2.0) - 0.5) * (entity.getRandom().nextBoolean()
                ? -1
                : 1);
            var f = entity.getFirstPassenger().getZ() + ((entity.getRandom().nextDouble() / 2.0) - 0.5) * (entity.getRandom().nextBoolean()
                ? -1
                : 1);
            if (entity.getFirstPassenger() instanceof Mob mob && !mob.isPersistenceRequired())
                mob.setPersistenceRequired();
            if (entity.isBiting() && entity.getFirstPassenger() != null) {
                if (!entity.level().isClientSide()) {
                    lastUpdateTime++;
                    if (Services.PLATFORM.isDevelopmentEnvironment())
                        AzureLib.LOGGER.debug(lastUpdateTime);
                }
                // Check if enough time has elapsed since the last update
                if (lastUpdateTime == 520L) {
                    if (entity instanceof ClassicAlienEntity classicAlienEntity)
                        GigCommonMethods.setAnimation(classicAlienEntity.animationDispatcher::sendExecution);
                }
                if (lastUpdateTime >= 600L) {
                    if (entity.getNavigation() != null)
                        entity.getNavigation().stop();
                    entity.getFirstPassenger()
                        .hurt(
                            GigDamageSources.of(entity.level(), GigDamageSources.EXECUTION),
                            Integer.MAX_VALUE
                        );
                    entity.heal(50);
                    if (entity.level().isClientSide)
                        entity.getFirstPassenger()
                            .level()
                            .addAlwaysVisibleParticle(
                                GigParticles.BLOOD.get(),
                                e,
                                yOffset,
                                f,
                                0.0,
                                -0.15,
                                0.0
                            );
                    entity.setIsBiting(false);
                    entity.setIsExecuting(false);
                    lastUpdateTime = 0;
                }
            } else if (entity.getFirstPassenger() != null) {
                if (!entity.level().isClientSide()) {
                    lastUpdateTime++;
                    if (Services.PLATFORM.isDevelopmentEnvironment())
                        AzureLib.LOGGER.debug(lastUpdateTime);
                }
                if (lastUpdateTime == 1200L) {
                    entity.setDeltaMovement(0, 0, 0);
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 100, false, false));
                    entity.setIsExecuting(true);
                    entity.setAggressive(false);
                    if (entity instanceof ClassicAlienEntity classicAlienEntity)
                        GigCommonMethods.setAnimation(classicAlienEntity.animationDispatcher::sendExecution);
                }
                if (lastUpdateTime >= 1280L) {
                    entity.getFirstPassenger()
                        .hurt(
                            GigDamageSources.of(entity.level(), GigDamageSources.EXECUTION),
                            Integer.MAX_VALUE
                        );
                    entity.heal(50);
                    if (entity.level().isClientSide)
                        entity.getFirstPassenger()
                            .level()
                            .addAlwaysVisibleParticle(
                                GigParticles.BLOOD.get(),
                                e,
                                yOffset,
                                f,
                                0.0,
                                -0.15,
                                0.0
                            );
                    lastUpdateTime = 0;
                }
            }
        }
    }
}
