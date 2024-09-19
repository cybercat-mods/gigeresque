package mods.cybercat.gigeresque.client.entity.render;

import mod.azure.azurelib.common.api.client.renderer.GeoEntityRenderer;
import mods.cybercat.gigeresque.client.entity.model.MoonlightHorrorTempleBeastEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.templebeast.MoonlightHorrorTempleBeastEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class MoonlightHorrorTempleBeastEntityRenderer extends GeoEntityRenderer<MoonlightHorrorTempleBeastEntity> {
    public MoonlightHorrorTempleBeastEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new MoonlightHorrorTempleBeastEntityModel());
        this.shadowRadius = 1.0f;
    }

    @Override
    protected float getDeathMaxRotation(MoonlightHorrorTempleBeastEntity entityLivingBaseIn) {
        return 0.0F;
    }

    @Override
    public float getMotionAnimThreshold(MoonlightHorrorTempleBeastEntity animatable) {
        return 0.005f;
    }
}
