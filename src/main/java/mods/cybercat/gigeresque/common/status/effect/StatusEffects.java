package mods.cybercat.gigeresque.common.status.effect;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.status.effect.impl.TraumaStatusEffect;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;
import mods.cybercat.gigeresque.common.util.InitializationTimer;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class StatusEffects implements GigeresqueInitializer {
	private StatusEffects() {
	}

	private static StatusEffects instance;

	synchronized public static StatusEffects getInstance() {
		if (instance == null) {
			instance = new StatusEffects();
		}
		return instance;
	}

	public static final StatusEffect TRAUMA = new TraumaStatusEffect().addAttributeModifier(
			EntityAttributes.GENERIC_MAX_HEALTH, "5e5ac802-7542-4418-b56e-548913950563", -0.5,
			EntityAttributeModifier.Operation.MULTIPLY_BASE);

	@Override
	public void initialize() {
		InitializationTimer.initializingBlock("StatusEffects", this::initializeImpl);
	}

	private void initializeImpl() {
		Registry.register(Registry.STATUS_EFFECT, new Identifier(Gigeresque.MOD_ID, "trauma"), TRAUMA);
	}
}
