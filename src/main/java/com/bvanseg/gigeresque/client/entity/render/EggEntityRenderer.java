package com.bvanseg.gigeresque.client.entity.render;

import com.bvanseg.gigeresque.client.entity.model.EggEntityModel;
import com.bvanseg.gigeresque.common.entity.impl.AlienEggEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class EggEntityRenderer extends GeoEntityRenderer<AlienEggEntity> {
    public EggEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new EggEntityModel());
        this.shadowRadius = 0.5f;
    }
}
