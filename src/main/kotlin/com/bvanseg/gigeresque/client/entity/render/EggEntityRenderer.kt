package com.bvanseg.gigeresque.client.entity.render

import com.bvanseg.gigeresque.client.entity.model.EggEntityModel
import com.bvanseg.gigeresque.common.entity.impl.AlienEggEntity
import net.minecraft.client.render.entity.EntityRendererFactory
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer

/**
 * @author Boston Vanseghi
 */
class EggEntityRenderer(context: EntityRendererFactory.Context) : GeoEntityRenderer<AlienEggEntity>(
    context,
    EggEntityModel()
) {
    init {
        this.shadowRadius = 0.5f
    }
}