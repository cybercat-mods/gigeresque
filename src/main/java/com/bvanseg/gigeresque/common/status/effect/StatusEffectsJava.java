package com.bvanseg.gigeresque.common.status.effect;

import com.bvanseg.gigeresque.common.GigeresqueJava;
import com.bvanseg.gigeresque.common.status.effect.impl.TraumaStatusEffectJava;
import com.bvanseg.gigeresque.common.util.GigeresqueInitializerJava;
import com.bvanseg.gigeresque.common.util.InitializationTimer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class StatusEffectsJava implements GigeresqueInitializerJava {
    public static final StatusEffect TRAUMA = new TraumaStatusEffectJava().addAttributeModifier(
            EntityAttributes.GENERIC_MAX_HEALTH,
            "5e5ac802-7542-4418-b56e-548913950563",
            -0.5,
            EntityAttributeModifier.Operation.MULTIPLY_BASE
    );

    @Override
    public void initialize() {
        InitializationTimer.initializingBlock("StatusEffects", this::initializeImpl);
    }

    private void initializeImpl() {
        Registry.register(Registry.STATUS_EFFECT, new Identifier(GigeresqueJava.MOD_ID, "trauma"), TRAUMA);
    }
}
