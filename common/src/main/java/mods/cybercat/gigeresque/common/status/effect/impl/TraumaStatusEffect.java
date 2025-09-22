package mods.cybercat.gigeresque.common.status.effect.impl;

import mod.azure.azurelib.core.object.Color;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class TraumaStatusEffect extends MobEffect {

    public TraumaStatusEffect() {
        super(MobEffectCategory.HARMFUL, Color.RED.getColor());
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    public static void effectRemoval(LivingEntity entity, MobEffectInstance mobEffectInstance) {
        if (entity.level().isClientSide || !(mobEffectInstance.getEffect().value() instanceof TraumaStatusEffect))
            return;
        if (entity instanceof Mob mob && mob.isNoAi())
            return;
        entity.setHealth(entity.getMaxHealth());
    }
}
