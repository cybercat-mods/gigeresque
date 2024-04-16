package mods.cybercat.gigeresque.client.entity.render;

import mod.azure.azurelib.common.api.client.renderer.GeoEntityRenderer;
import mods.cybercat.gigeresque.client.entity.model.StalkerEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.mutant.StalkerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class StalkerEntityRenderer extends GeoEntityRenderer<StalkerEntity> {
    public StalkerEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new StalkerEntityModel());
        this.shadowRadius = 1.0f;
    }

    @Override
    protected float getDeathMaxRotation(StalkerEntity entityLivingBaseIn) {
        return 0.0F;
    }

    @Override
    public float getMotionAnimThreshold(StalkerEntity animatable) {
        return 0.005f;
    }
}
