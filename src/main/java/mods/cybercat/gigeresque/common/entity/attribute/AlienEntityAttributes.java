package mods.cybercat.gigeresque.common.entity.attribute;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;

public class AlienEntityAttributes implements GigeresqueInitializer {

    public static final double SABOTAGE_THRESHOLD = 0.85;

    public static final double SELF_PRESERVE_THRESHOLD = 0.35;

    public static final RangedAttribute INTELLIGENCE_ATTRIBUTE = new RangedAttribute(
        "attribute.name.gigeresque.intelligence",
        0.5,
        0.0,
        1.0
    );

    @Override
    public void initialize() {
        Registry.register(BuiltInRegistries.ATTRIBUTE, Constants.modResource("attribute.intelligence"), INTELLIGENCE_ATTRIBUTE);
    }
}
