package com.bvanseg.gigeresque.common.status.effect.impl;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import java.awt.*;

public class TraumaStatusEffect extends StatusEffect {
    public TraumaStatusEffect() {
        super(StatusEffectCategory.HARMFUL, Color.red.getRGB());
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
