package mods.cybercat.gigeresque.common.status.effect.impl;

import java.awt.Color;

import mods.cybercat.gigeresque.common.status.effect.StatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class DNAStatusEffect extends StatusEffect {
	public DNAStatusEffect() {
		super(StatusEffectCategory.HARMFUL, Color.red.getRGB());
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}
	
	@Override
	public boolean isInstant() {
		return true;
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		super.applyUpdateEffect(entity, amplifier);
		if (this == StatusEffects.DNA)
			entity.heal(0);
	}
}