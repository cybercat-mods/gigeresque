package mods.cybercat.gigeresque.common.status.effect.impl;

import mod.azure.azurelib.core.object.Color;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.particle.Particles;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.source.GigDamageSources;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

public class ImpregnationStatusEffect extends MobEffect {

    public ImpregnationStatusEffect() {
        super(MobEffectCategory.HARMFUL, Color.DARK_GRAY.getColor());
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean isInstantenous() {
        return false;
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
        super.applyEffectTick(livingEntity, amplifier);
        if (GigEntityUtils.isTargetHostable(livingEntity) && this == GigStatusEffects.IMPREGNATION) {
            this.handleStatusEffects(
                livingEntity,
                (int) Gigeresque.config.impregnationTickTimer,
                MobEffects.HUNGER,
                MobEffects.WEAKNESS,
                MobEffects.DIG_SLOWDOWN
            );
        }
    }

    private void handleStatusEffects(@NotNull LivingEntity livingEntity, int ticks, MobEffect... statusEffects) {
        for (MobEffect effect : statusEffects) {
            if (!livingEntity.hasEffect(effect)) {
                livingEntity.addEffect(new MobEffectInstance(effect, ticks, 3, true, true));
            }
        }
    }

    private void applyParticle(@NotNull LivingEntity livingEntity) {
        var random = livingEntity.getRandom();
        if (livingEntity.isAlive() && livingEntity.level().isClientSide) {
            var yOffset = livingEntity.getEyeY() - ((livingEntity.getEyeY() - livingEntity.blockPosition().getY()) / 2.0);
            var customX = livingEntity.getX() + ((random.nextDouble() / 2.0) - 0.5) * (random.nextBoolean() ? -1 : 1);
            var customZ = livingEntity.getZ() + ((random.nextDouble() / 2.0) - 0.5) * (random.nextBoolean() ? -1 : 1);
            for (var i = 0; i < 1 + (int) (livingEntity.getMaxHealth() - livingEntity.getHealth()); i++)
                livingEntity.level()
                    .addAlwaysVisibleParticle(
                        Particles.BLOOD,
                        customX,
                        yOffset,
                        customZ,
                        0.0,
                        -0.15,
                        0.0
                    );
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, @NotNull AttributeMap attributes, int amplifier) {
        if (Constants.isCreativeSpecPlayer.test(entity))
            return;
        if (!GigEntityUtils.isTargetHostable(entity))
            return;
        LivingEntity burster = createBurster(entity);
        if (burster != null) {
            setBursterProperties(entity, burster);
            entity.level().addFreshEntity(burster);
            entity.level()
                .playSound(
                    entity,
                    entity.blockPosition(),
                    GigSounds.CHESTBURSTING,
                    SoundSource.NEUTRAL,
                    2.0f,
                    1.0f
                );
            entity.hurt(GigDamageSources.of(entity.level(), GigDamageSources.CHESTBURSTING), entity.getMaxHealth());
        }
    }

    private static LivingEntity createBurster(LivingEntity entity) {
        var defaultBurster = Entities.CHESTBURSTER.create(entity.level());
        if (!entity.hasEffect(GigStatusEffects.SPORE) && !entity.hasEffect(GigStatusEffects.DNA)) {
            if (entity.getType().is(GigTags.RUNNER_HOSTS)) {
                var runnerBurster = Entities.RUNNERBURSTER.create(entity.level());
                if (runnerBurster != null) {
                    runnerBurster.setHostId("runner");
                    return runnerBurster;
                }
            } else if (entity.getType().is(GigTags.AQUATIC_HOSTS)) {
                return Entities.AQUATIC_CHESTBURSTER.create(entity.level());
            }
        } else if (entity.getType().is(GigTags.NEOHOST) && entity.hasEffect(GigStatusEffects.SPORE)) {
            return Entities.NEOBURSTER.create(entity.level());
        } else if (entity.getType().is(GigTags.CLASSIC_HOSTS) && entity.hasEffect(GigStatusEffects.DNA)) {
            return Entities.SPITTER.create(entity.level());
        }
        return defaultBurster;
    }

    private static void setBursterProperties(LivingEntity entity, LivingEntity burster) {
        if (entity.hasCustomName()) {
            burster.setCustomName(entity.getCustomName());
        }
        burster.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 10), burster);
        burster.moveTo(entity.blockPosition(), entity.getYRot(), entity.getXRot());
    }
}
