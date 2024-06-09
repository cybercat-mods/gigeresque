package mods.cybercat.gigeresque.common.status.effect;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.status.effect.impl.*;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public record GigStatusEffects() implements GigeresqueInitializer {

    public static final Holder<MobEffect> TRAUMA = MobEffects.register("trauma", new TraumaStatusEffect().addAttributeModifier(Attributes.MAX_HEALTH, "5e5ac802-7542-4418-b56e-548913950563", -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final Holder<MobEffect> ACID = MobEffects.register("acid", new AcidStatusEffect());
    public static final Holder<MobEffect> DNA = MobEffects.register("dna_disintegration", new DNAStatusEffect());
    public static final Holder<MobEffect> SPORE = MobEffects.register("neo_spore", new SporeStatusEffect());
    public static final Holder<MobEffect> IMPREGNATION = MobEffects.register("impregnation", new ImpregnationStatusEffect());
    public static final Holder<MobEffect> EGGMORPHING = MobEffects.register("eggmorphing", new EggMorphingStatusEffect());
    private static GigStatusEffects instance;

    public static synchronized GigStatusEffects getInstance() {
        if (instance == null) instance = new GigStatusEffects();
        return instance;
    }

    @Override
    public void initialize() {
        Registry.register(BuiltInRegistries.MOB_EFFECT, Constants.modResource("trauma"), TRAUMA.value());
        Registry.register(BuiltInRegistries.MOB_EFFECT, Constants.modResource("acid"), ACID.value());
        Registry.register(BuiltInRegistries.MOB_EFFECT, Constants.modResource("dna_disintegration"), DNA.value());
        Registry.register(BuiltInRegistries.MOB_EFFECT, Constants.modResource("neo_spore"), SPORE.value());
        Registry.register(BuiltInRegistries.MOB_EFFECT, Constants.modResource("impregnation"), IMPREGNATION.value());
        Registry.register(BuiltInRegistries.MOB_EFFECT, Constants.modResource("eggmorphing"), EGGMORPHING.value());
    }
}
