package mods.cybercat.gigeresque.client.entity.render;

import mod.azure.azurelib.common.api.client.renderer.GeoEntityRenderer;
import mods.cybercat.gigeresque.client.entity.model.DraconicTempleBeastEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.templebeast.DraconicTempleBeastEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class DraconicTempleBeastEntityRenderer extends GeoEntityRenderer<DraconicTempleBeastEntity> {
    public DraconicTempleBeastEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new DraconicTempleBeastEntityModel());
        this.shadowRadius = 1.0f;
    }

    @Override
    protected float getDeathMaxRotation(DraconicTempleBeastEntity entityLivingBaseIn) {
        return 0.0F;
    }

    @Override
    public float getMotionAnimThreshold(DraconicTempleBeastEntity animatable) {
        return 0.005f;
    }
}
