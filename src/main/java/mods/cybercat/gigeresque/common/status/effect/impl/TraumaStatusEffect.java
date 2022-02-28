package mods.cybercat.gigeresque.common.status.effect.impl;

import java.awt.Color;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class TraumaStatusEffect extends StatusEffect {
	public TraumaStatusEffect() {
		super(StatusEffectCategory.HARMFUL, Color.red.getRGB());
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}
}
