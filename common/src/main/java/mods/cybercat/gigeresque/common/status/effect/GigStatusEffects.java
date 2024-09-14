package mods.cybercat.gigeresque.common.status.effect;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.status.effect.impl.*;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public record GigStatusEffects() implements CommonStatusEffectRegistryInterface {

    public static final Holder<MobEffect> TRAUMA = CommonStatusEffectRegistryInterface.registerStatusEffect(
            CommonMod.MOD_ID,
            "trauma",
            () -> new TraumaStatusEffect()
                    .addAttributeModifier(
                            Attributes.MAX_HEALTH,
                            ResourceLocation.fromNamespaceAndPath(
                                    CommonMod.MOD_ID,
                                    "health"),
                            -0.5,
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final Holder<MobEffect> ACID = CommonStatusEffectRegistryInterface.registerStatusEffect(
            CommonMod.MOD_ID,
            "acid",
            AcidStatusEffect::new);
    public static final Holder<MobEffect> DNA = CommonStatusEffectRegistryInterface.registerStatusEffect(
            CommonMod.MOD_ID,
            "dna_disintegration",
            DNAStatusEffect::new);
    public static final Holder<MobEffect> SPORE = CommonStatusEffectRegistryInterface.registerStatusEffect(
            CommonMod.MOD_ID,
            "neo_spore",
            SporeStatusEffect::new);
    public static final Holder<MobEffect> IMPREGNATION = CommonStatusEffectRegistryInterface.registerStatusEffect(
            CommonMod.MOD_ID,
            "impregnation",
            ImpregnationStatusEffect::new);
    public static final Holder<MobEffect> EGGMORPHING = CommonStatusEffectRegistryInterface.registerStatusEffect(
            CommonMod.MOD_ID,
            "eggmorphing",
            EggMorphingStatusEffect::new);
    public static final Holder<MobEffect> DUNGEON_EFFECT = CommonStatusEffectRegistryInterface.registerStatusEffect(CommonMod.MOD_ID,
            "dungeon_status",
            PandorasBoxStatusEffect::new);

    public static void initialize() {
    }
}
