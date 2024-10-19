package mods.cybercat.gigeresque.client.entity.render;

import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.client.entity.model.HammerpedeEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.mutant.HammerpedeEntity;

public class HammerpedeEntityRenderer extends GeoEntityRenderer<HammerpedeEntity> {

    public HammerpedeEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new HammerpedeEntityModel());
        this.shadowRadius = 0.5f;
    }

    @Override
    protected float getDeathMaxRotation(HammerpedeEntity entityLivingBaseIn) {
        return 0.0F;
    }
}
