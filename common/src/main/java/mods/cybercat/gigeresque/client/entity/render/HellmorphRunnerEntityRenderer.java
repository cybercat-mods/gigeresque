package mods.cybercat.gigeresque.client.entity.render;

import mod.azure.azurelib.common.api.client.renderer.GeoEntityRenderer;
import mods.cybercat.gigeresque.client.entity.model.HellmorphRunnerEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.hellmorphs.HellmorphRunnerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class HellmorphRunnerEntityRenderer extends GeoEntityRenderer<HellmorphRunnerEntity> {
    public HellmorphRunnerEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new HellmorphRunnerEntityModel());
        this.shadowRadius = 1.0f;
    }

    @Override
    protected float getDeathMaxRotation(HellmorphRunnerEntity entityLivingBaseIn) {
        return 0.0F;
    }

    @Override
    public float getMotionAnimThreshold(HellmorphRunnerEntity animatable) {
        return 0.005f;
    }
}
