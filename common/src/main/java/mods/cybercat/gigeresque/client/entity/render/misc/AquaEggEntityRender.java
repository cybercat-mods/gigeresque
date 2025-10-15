package mods.cybercat.gigeresque.client.entity.render.misc;

import mod.azure.azurelib.common.render.entity.AzEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.misc.AquaEggEntity;

public class AquaEggEntityRender extends AzEntityRenderer<AquaEggEntity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/entity/egg/egg.geo.json");

    public AquaEggEntityRender(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<AquaEggEntity>builder(
                EntityModels.AQUA_EGG,
                EntityTextures.AQUA_EGG
            )
                .setDeathMaxRotation(0.0F)
                .setScale(entity -> 0.2f + (entity.getGrowth() / entity.getMaxGrowth()) / 5f)
                .build(),
            context
        );
    }

}
