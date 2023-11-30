package mods.cybercat.gigeresque.client.entity.render;

import mod.azure.azurelib.renderer.GeoEntityRenderer;
import mods.cybercat.gigeresque.client.entity.model.RavenousTempleBeastEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.extra.RavenousTempleBeastEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@Environment(EnvType.CLIENT)
public class RavenousTempleBeastEntityRenderer extends GeoEntityRenderer<RavenousTempleBeastEntity> {
    public RavenousTempleBeastEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new RavenousTempleBeastEntityModel());
        this.shadowRadius = 1.0f;
    }

    @Override
    protected float getDeathMaxRotation(RavenousTempleBeastEntity entityLivingBaseIn) {
        return 0.0F;
    }

    @Override
    public float getMotionAnimThreshold(RavenousTempleBeastEntity animatable) {
        return 0.005f;
    }
}
