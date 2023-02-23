package mods.cybercat.gigeresque.common.status.effect.impl;

import java.awt.Color;

import mods.cybercat.gigeresque.common.config.GigeresqueConfig;
import mods.cybercat.gigeresque.common.source.GigDamageSources;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class AcidStatusEffect extends MobEffect {
	public AcidStatusEffect() {
		super(MobEffectCategory.HARMFUL, Color.green.getRGB());
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		super.applyEffectTick(entity, amplifier);
		if (this == GigStatusEffects.ACID)
			entity.hurt(GigDamageSources.ACID, GigeresqueConfig.acidDamage);
	}
}