package mods.cybercat.gigeresque.common.status.effect;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.status.effect.impl.AcidStatusEffect;
import mods.cybercat.gigeresque.common.status.effect.impl.DNAStatusEffect;
import mods.cybercat.gigeresque.common.status.effect.impl.TraumaStatusEffect;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class GigStatusEffects implements GigeresqueInitializer {
	private GigStatusEffects() {
	}

	private static GigStatusEffects instance;

	synchronized public static GigStatusEffects getInstance() {
		if (instance == null)
			instance = new GigStatusEffects();
		return instance;
	}

	public static final MobEffect TRAUMA = new TraumaStatusEffect().addAttributeModifier(Attributes.MAX_HEALTH,
			"5e5ac802-7542-4418-b56e-548913950563", -0.5, AttributeModifier.Operation.MULTIPLY_BASE);

	public static final MobEffect ACID = new AcidStatusEffect();

	public static final MobEffect DNA = new DNAStatusEffect();

	@Override
	public void initialize() {
		Registry.register(Registry.MOB_EFFECT, Constants.modResource("trauma"), TRAUMA);
		Registry.register(Registry.MOB_EFFECT, Constants.modResource("acid"), ACID);
		Registry.register(Registry.MOB_EFFECT, Constants.modResource("dna_disintegration"), DNA);
	}
}
