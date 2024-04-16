package mods.cybercat.gigeresque.common.status.effect.impl;

import mod.azure.azurelib.common.internal.common.core.object.Color;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.source.GigDamageSources;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.tags.GigTags;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class SporeStatusEffect extends MobEffect {

    public SporeStatusEffect() {
        super(MobEffectCategory.HARMFUL, Color.BLACK.getColor());
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean isInstantenous() {
        return false;
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        super.applyEffectTick(entity, amplifier);
        if (this == GigStatusEffects.SPORE) entity.heal(0);
    }

    public static void effectRemoval(LivingEntity entity, MobEffectInstance mobEffectInstance) {
        var neoBurster = Entities.NEOBURSTER.create(entity.level());
        if (!entity.getType().is(GigTags.GIG_ALIENS) && entity.getType().is(
                GigTags.NEOHOST) && (!(entity instanceof Player) || (entity instanceof Player playerEntity && !(playerEntity.isCreative() || playerEntity.isSpectator())))) {
            assert neoBurster != null;
            neoBurster.moveTo(entity.blockPosition(), entity.getYRot(), entity.getXRot());
            spawnEffects(entity.level(), entity);
            entity.level().addFreshEntity(neoBurster);
            entity.hurt(GigDamageSources.of(entity.level(), GigDamageSources.SPORE), Integer.MAX_VALUE);
        }
    }

    private static void spawnEffects(Level world, LivingEntity entity) {
        if (!world.isClientSide()) {
            for (var i = 0; i < 2; i++)
                ((ServerLevel) world).sendParticles(ParticleTypes.POOF, entity.getX() + 0.5, entity.getY(),
                        entity.getZ() + 0.5, 1, entity.getRandom().nextGaussian() * 0.02,
                        entity.getRandom().nextGaussian() * 0.02, entity.getRandom().nextGaussian() * 0.02,
                        0.15000000596046448);
        }
    }

}