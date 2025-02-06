package mods.cybercat.gigeresque.client.entity.render.mutant;

import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.mutant.PopperAnimator;
import mods.cybercat.gigeresque.common.entity.impl.mutant.PopperEntity;

public class PopperEntityRenderer extends AzEntityRenderer<PopperEntity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/entity/popper/popper.geo.json");

    public PopperEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<PopperEntity>builder(
                MODEL,
                EntityTextures.POPPER
            )
                .setAnimatorProvider(PopperAnimator::new)
                .setDeathMaxRotation(0.0F)
                .build(),
            context
        );
        this.shadowRadius = 0.5f;
    }
}
