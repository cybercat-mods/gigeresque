package mods.cybercat.gigeresque.common.status.effect.impl;

import mod.azure.azurelib.core.object.Color;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.source.GigDamageSources;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class AcidStatusEffect extends MobEffect {
    public AcidStatusEffect() {
        super(MobEffectCategory.HARMFUL, Color.GREEN.getColor());
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        super.applyEffectTick(entity, amplifier);
        entity.hurt(GigDamageSources.of(entity.level(), GigDamageSources.ACID), Gigeresque.config.acidDamage * amplifier);
    }
}