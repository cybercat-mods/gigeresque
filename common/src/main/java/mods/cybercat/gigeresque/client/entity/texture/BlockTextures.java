package mods.cybercat.gigeresque.client.entity.texture;

import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;

public record BlockTextures() {

    public static final ResourceLocation JAR_TEXTURE = Constants.modResource("textures/block/jar/jar.png");

    public static final ResourceLocation SARCOPHAGUS_TEXTURE = Constants.modResource("textures/block/sarcophagus/sarcophagus.png");

    public static final ResourceLocation SITTING_IDOL_TEXTURE = Constants.modResource("textures/block/sittingidol/sittingidol.png");

    public static final ResourceLocation SPORE_TEXTURE = Constants.modResource(
        "textures/block/neomorph_spore_pods/neomorph_spore_pods.png"
    );
}
