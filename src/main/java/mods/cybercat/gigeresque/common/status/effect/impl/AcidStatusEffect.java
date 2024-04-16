package mods.cybercat.gigeresque.common.status.effect.impl;

import mod.azure.azurelib.common.internal.common.core.object.Color;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.source.GigDamageSources;
import mods.cybercat.gigeresque.common.tags.GigTags;
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
        if (!entity.getType().is(GigTags.ACID_RESISTANT_ENTITY))
            entity.hurt(GigDamageSources.of(entity.level(), GigDamageSources.ACID),
                    Gigeresque.config.acidDamage * amplifier);
    }
}