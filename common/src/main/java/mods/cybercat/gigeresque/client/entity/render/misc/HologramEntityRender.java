package mods.cybercat.gigeresque.client.entity.render.misc;

import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.rewrite.render.layer.AzAutoGlowingLayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.animators.misc.HologramAnimator;
import mods.cybercat.gigeresque.common.entity.impl.misc.HologramEntity;

public class HologramEntityRender extends AzEntityRenderer<HologramEntity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/entity/engineer_hologram/engineer_hologram.geo.json");

    private static final ResourceLocation TEX = Constants.modResource("textures/entity/engineer_hologram/engineer_hologram.png");

    private static final RenderType RENDER_TYPE = RenderType.entityTranslucentCull(TEX);

    public HologramEntityRender(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<HologramEntity>builder(MODEL, TEX)
                .setAnimatorProvider(HologramAnimator::new)
                .setDeathMaxRotation(0.0F)
                .setRenderType(RENDER_TYPE)
                .addRenderLayer(new AzAutoGlowingLayer<>())
                .build(),
            context
        );
    }
}
