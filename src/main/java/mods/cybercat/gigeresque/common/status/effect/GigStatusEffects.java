package mods.cybercat.gigeresque.common.status.effect;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.status.effect.impl.AcidStatusEffect;
import mods.cybercat.gigeresque.common.status.effect.impl.DNAStatusEffect;
import mods.cybercat.gigeresque.common.status.effect.impl.TraumaStatusEffect;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class GigStatusEffects implements GigeresqueInitializer {
	private GigStatusEffects() {
	}

	private static GigStatusEffects instance;

	synchronized public static GigStatusEffects getInstance() {
		if (instance == null) {
			instance = new GigStatusEffects();
		}
		return instance;
	}

	public static final StatusEffect TRAUMA = new TraumaStatusEffect().addAttributeModifier(
			EntityAttributes.GENERIC_MAX_HEALTH, "5e5ac802-7542-4418-b56e-548913950563", -0.5,
			EntityAttributeModifier.Operation.MULTIPLY_BASE);

	public static final StatusEffect ACID = new AcidStatusEffect();

	public static final StatusEffect DNA = new DNAStatusEffect();

	@Override
	public void initialize() {
		Registry.register(Registry.STATUS_EFFECT, new Identifier(Gigeresque.MOD_ID, "trauma"), TRAUMA);
		Registry.register(Registry.STATUS_EFFECT, new Identifier(Gigeresque.MOD_ID, "acid"), ACID);
		Registry.register(Registry.STATUS_EFFECT, new Identifier(Gigeresque.MOD_ID, "dna_disintegration"), DNA);
	}
}
