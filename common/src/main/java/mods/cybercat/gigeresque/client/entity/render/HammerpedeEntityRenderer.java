package mods.cybercat.gigeresque.client.entity.render;

import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.mutant.HammerpedeAnimator;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.common.entity.impl.mutant.HammerpedeEntity;
import net.minecraft.resources.ResourceLocation;

public class HammerpedeEntityRenderer extends AzEntityRenderer<HammerpedeEntity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/entity/hammerpede/hammerpede.geo.json");

    public HammerpedeEntityRenderer(EntityRendererProvider.Context context) {
        super(
                AzEntityRendererConfig.<HammerpedeEntity>builder(
                                MODEL,
                                EntityTextures.HAMMERPEDE
                        )
                        .setAnimatorProvider(HammerpedeAnimator::new)
                        .setDeathMaxRotation(0.0F)
                        .build(),
                context
        );
        this.shadowRadius = 0.5f;
    }
}
