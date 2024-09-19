package mods.cybercat.gigeresque.client.entity.render;

import mod.azure.azurelib.common.api.client.renderer.GeoEntityRenderer;
import mods.cybercat.gigeresque.client.entity.model.BaphomorphEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.hellmorphs.BaphomorphEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class BaphomorphEntityRenderer extends GeoEntityRenderer<BaphomorphEntity> {
    public BaphomorphEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new BaphomorphEntityModel());
        this.shadowRadius = 1.0f;
    }

    @Override
    protected float getDeathMaxRotation(BaphomorphEntity entityLivingBaseIn) {
        return 0.0F;
    }

    @Override
    public float getMotionAnimThreshold(BaphomorphEntity animatable) {
        return 0.005f;
    }
}
