package mods.cybercat.gigeresque.client.entity.render;

import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.client.entity.model.PopperEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.mutant.PopperEntity;

public class PopperEntityRenderer extends GeoEntityRenderer<PopperEntity> {

    public PopperEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new PopperEntityModel());
        this.shadowRadius = 0.5f;
    }

    @Override
    protected float getDeathMaxRotation(PopperEntity entityLivingBaseIn) {
        return 0.0F;
    }
}
