package mods.cybercat.gigeresque.client.entity.render;

import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.client.entity.model.EggEntityModel;
import mods.cybercat.gigeresque.client.entity.render.feature.EggDyingFeatureRenderer;
import mods.cybercat.gigeresque.common.entity.impl.classic.AlienEggEntity;

public class EggEntityRenderer extends GeoEntityRenderer<AlienEggEntity> {

    public EggEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new EggEntityModel());
        this.addRenderLayer(new EggDyingFeatureRenderer(this));
        this.shadowRadius = 0.5f;
    }

    @Override
    protected float getDeathMaxRotation(AlienEggEntity entityLivingBaseIn) {
        return 0.0F;
    }
}
