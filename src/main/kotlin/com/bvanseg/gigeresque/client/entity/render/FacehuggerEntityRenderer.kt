package com.bvanseg.gigeresque.client.entity.render

import com.bvanseg.gigeresque.client.entity.model.FacehuggerEntityModel
import com.bvanseg.gigeresque.common.entity.impl.FacehuggerEntity
import net.minecraft.client.render.entity.EntityRendererFactory
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer

/**
 * @author Boston Vanseghi
 */
class FacehuggerEntityRenderer(context: EntityRendererFactory.Context) : GeoEntityRenderer<FacehuggerEntity>(
    context,
    FacehuggerEntityModel()
) {
    init {
        this.shadowRadius = 0.2f
    }
}