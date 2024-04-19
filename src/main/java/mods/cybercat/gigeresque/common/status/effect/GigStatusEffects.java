package mods.cybercat.gigeresque.common.status.effect;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.status.effect.impl.*;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public record GigStatusEffects() implements GigeresqueInitializer {

    public static final MobEffect TRAUMA = new TraumaStatusEffect().addAttributeModifier(Attributes.MAX_HEALTH, "5e5ac802-7542-4418-b56e-548913950563", -0.5, AttributeModifier.Operation.MULTIPLY_BASE);
    public static final MobEffect ACID = new AcidStatusEffect();
    public static final MobEffect DNA = new DNAStatusEffect();
    public static final MobEffect SPORE = new SporeStatusEffect();
    public static final MobEffect IMPREGNATION = new ImpregnationStatusEffect();
    public static final MobEffect EGGMORPHING = new EggMorphingStatusEffect();
    private static GigStatusEffects instance;

    public static synchronized GigStatusEffects getInstance() {
        if (instance == null) instance = new GigStatusEffects();
        return instance;
    }

    @Override
    public void initialize() {
        Registry.register(BuiltInRegistries.MOB_EFFECT, Constants.modResource("trauma"), TRAUMA);
        Registry.register(BuiltInRegistries.MOB_EFFECT, Constants.modResource("acid"), ACID);
        Registry.register(BuiltInRegistries.MOB_EFFECT, Constants.modResource("dna_disintegration"), DNA);
        Registry.register(BuiltInRegistries.MOB_EFFECT, Constants.modResource("neo_spore"), SPORE);
        Registry.register(BuiltInRegistries.MOB_EFFECT, Constants.modResource("impregnation"), IMPREGNATION);
        Registry.register(BuiltInRegistries.MOB_EFFECT, Constants.modResource("eggmorphing"), EGGMORPHING);
    }
}
