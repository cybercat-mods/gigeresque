package mods.cybercat.gigeresque.common.entity.attribute;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AlienEntityAttributes implements GigeresqueInitializer {
	public static final double SABOTAGE_THRESHOLD = 0.85;
	public static final double SELF_PRESERVE_THRESHOLD = 0.35;

	public static final ClampedEntityAttribute INTELLIGENCE_ATTRIBUTE = new ClampedEntityAttribute(
			"attribute.name.gigeresque.intelligence", 0.5, 0.0, 1.0);

	@Override
	public void initialize() {
		Registry.register(Registry.ATTRIBUTE, new Identifier(Gigeresque.MOD_ID, "attribute.intelligence"),
				INTELLIGENCE_ATTRIBUTE);
	}
}
