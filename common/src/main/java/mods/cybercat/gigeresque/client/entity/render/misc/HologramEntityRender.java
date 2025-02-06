package mods.cybercat.gigeresque.client.entity.render.misc;

import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.rewrite.render.layer.AzAutoGlowingLayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.misc.HologramAnimator;
import mods.cybercat.gigeresque.common.entity.impl.misc.HologramEntity;

public class HologramEntityRender extends AzEntityRenderer<HologramEntity> {

    private static final RenderType RENDER_TYPE = RenderType.entityTranslucentCull(EntityTextures.ENGINEER_HOLOGRAM);

    public HologramEntityRender(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<HologramEntity>builder(EntityModels.ENGINEER_HOLOGRAM, EntityTextures.ENGINEER_HOLOGRAM)
                .setAnimatorProvider(HologramAnimator::new)
                .setDeathMaxRotation(0.0F)
                .setRenderType(RENDER_TYPE)
                .addRenderLayer(new AzAutoGlowingLayer<>())
                .build(),
            context
        );
    }
}
