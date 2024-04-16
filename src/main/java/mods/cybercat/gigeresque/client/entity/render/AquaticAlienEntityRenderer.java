package mods.cybercat.gigeresque.client.entity.render;

import mod.azure.azurelib.common.api.client.renderer.GeoEntityRenderer;
import mods.cybercat.gigeresque.client.entity.model.AquaticAlienEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.aqua.AquaticAlienEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class AquaticAlienEntityRenderer extends GeoEntityRenderer<AquaticAlienEntity> {
    public AquaticAlienEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new AquaticAlienEntityModel());
        this.shadowRadius = 0.5f;
    }

    @Override
    protected float getDeathMaxRotation(AquaticAlienEntity entityLivingBaseIn) {
        return 0;
    }
}
