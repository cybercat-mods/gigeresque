package com.bvanseg.gigeresque.common.status.effect.impl;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import java.awt.*;

public class TraumaStatusEffectJava extends StatusEffect {
    public TraumaStatusEffectJava() {
        super(StatusEffectCategory.HARMFUL, Color.red.getRGB());
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
