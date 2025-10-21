package mods.cybercat.gigeresque.common.status.effect.impl;

import mod.azure.azurelib.core.object.Color;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.source.GigDamageSources;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.util.DamageSourceUtils;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

public class ImpregnationStatusEffect extends MobEffect {

    public ImpregnationStatusEffect() {
        super(MobEffectCategory.HARMFUL, Color.DARK_GRAY.getColor());
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onEffectAdded(LivingEntity livingEntity, int amplifier) {
        if (GigEntityUtils.isTargetHostable(livingEntity) && this == GigStatusEffects.IMPREGNATION) {
            this.handleStatusEffects(
                livingEntity,
                (int) CommonMod.config.entityConfigs.facehuggerConfigs.impregnationTickTimer,
                MobEffects.HUNGER,
                MobEffects.WEAKNESS,
                MobEffects.DIG_SLOWDOWN
            );
        }
        super.onEffectAdded(livingEntity, amplifier);
    }

    @SafeVarargs
    private void handleStatusEffects(@NotNull LivingEntity livingEntity, int ticks, Holder<MobEffect>... statusEffects) {
        for (Holder<MobEffect> effect : statusEffects)
            if (!livingEntity.hasEffect(effect))
                livingEntity.addEffect(new MobEffectInstance(effect, ticks, 3, true, true));
    }

    public static void effectRemoval(LivingEntity entity, MobEffectInstance mobEffectInstance) {
        if (Constants.isCreativeSpecPlayer.test(entity))
            return;
        if (!GigEntityUtils.isTargetHostable(entity))
            return;
        if (entity.level().isClientSide || !(mobEffectInstance.getEffect().value() instanceof ImpregnationStatusEffect))
            return;
        if (entity instanceof Mob mob && mob.isNoAi())
            return;
        if (entity.hasEffect(GigStatusEffects.TRAUMA))
            return;
        var burster = GigEntityUtils.spawnBurster(entity);
        if (burster != null) {
            setBursterProperties(entity, burster);
            entity.level().addFreshEntity(burster);
            entity.level().playSound(entity, entity.blockPosition(), GigSounds.CHESTBURSTING.get(), SoundSource.NEUTRAL, 2.0f, 1.0f);
            if (Constants.isNotCreativeSpecPlayer.test(entity))
                DamageSourceUtils.damageArmor(entity.getItemBySlot(EquipmentSlot.CHEST), entity.getRandom(), 5, 10);
            entity.hurt(GigDamageSources.of(entity.level(), GigDamageSources.CHESTBURSTING), Float.MAX_VALUE);
        }
    }

    private static void setBursterProperties(LivingEntity entity, LivingEntity burster) {
        if (entity.hasCustomName())
            burster.setCustomName(entity.getCustomName());
        if (entity instanceof LivingEntity livingEntity) {
            for (var effect : livingEntity.getActiveEffects()) {
                burster.addEffect(new MobEffectInstance(effect));
            }
        }
        burster.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 10), burster);
        burster.setPos(entity.getX(), entity.getY(), entity.getZ());
    }
}
