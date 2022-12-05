package mods.cybercat.gigeresque.common.entity.attribute;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class AlienEntityAttributes implements GigeresqueInitializer {
	public static final double SABOTAGE_THRESHOLD = 0.85;
	public static final double SELF_PRESERVE_THRESHOLD = 0.35;

	public static final RangedAttribute INTELLIGENCE_ATTRIBUTE = new RangedAttribute(
			"attribute.name.gigeresque.intelligence", 0.5, 0.0, 1.0);

	@Override
	public void initialize() {
		Registry.register(BuiltInRegistries.ATTRIBUTE, new ResourceLocation(Gigeresque.MOD_ID, "attribute.intelligence"),
				INTELLIGENCE_ATTRIBUTE);
	}
}
