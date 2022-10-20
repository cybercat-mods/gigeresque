package mods.cybercat.gigeresque.common.status.effect.impl;

import java.awt.Color;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class TraumaStatusEffect extends MobEffect {
	public TraumaStatusEffect() {
		super(MobEffectCategory.HARMFUL, Color.red.getRGB());
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
