package mods.cybercat.gigeresque.common.status.effect.impl;

import mod.azure.azurelib.core.object.Color;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.source.GigDamageSources;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.tags.GigTags;

public class AcidStatusEffect extends MobEffect {

    public AcidStatusEffect() {
        super(MobEffectCategory.HARMFUL, Color.GREEN.getColor());
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        super.applyEffectTick(entity, amplifier);
        if (entity.hasEffect(GigStatusEffects.EGGMORPHING)) {
            return true;
        }
        if (entity.getVehicle() instanceof AlienEntity) {
            return true;
        }
        if (!entity.getType().is(GigTags.ACID_RESISTANT_ENTITY) && entity.tickCount % 40 == 0)
            entity.hurt(
                GigDamageSources.of(entity.level(), GigDamageSources.ACID),
                CommonMod.config.alienblockConfigs.acidDamage * amplifier
            );
        return super.applyEffectTick(entity, amplifier);
    }
}
