package com.bvanseg.gigeresque.client.entity.render;

import com.bvanseg.gigeresque.client.entity.model.EggEntityModelJava;
import com.bvanseg.gigeresque.common.entity.impl.AlienEggEntityJava;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class EggEntityRendererJava extends GeoEntityRenderer<AlienEggEntityJava> {
    public EggEntityRendererJava(EntityRendererFactory.Context context) {
        super(context, new EggEntityModelJava());
        this.shadowRadius = 0.5f;
    }
}
