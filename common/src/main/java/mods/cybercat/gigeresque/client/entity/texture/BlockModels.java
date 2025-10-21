package mods.cybercat.gigeresque.client.entity.texture;

import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;

public record BlockModels() {

    public static final ResourceLocation EGG_PETRIFIED_MODEL = Constants.modResource("geo/block/petrified/egg_petrified.geo.json");

    public static final ResourceLocation JAR_MODEL = Constants.modResource("geo/block/jar/jar.geo.json");

    public static final ResourceLocation SARCOPHAGUS_MODEL = Constants.modResource("geo/block/sarcophagus/sarcophagus.geo.json");

    public static final ResourceLocation SITTING_IDOL_MODEL = Constants.modResource("geo/block/sittingidol/sittingidol.geo.json");

    public static final ResourceLocation SPORE_MODEL = Constants.modResource("geo/block/neomorph_spore_pods/neomorph_spore_pods.geo.json");
}
