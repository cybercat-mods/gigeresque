package com.bvanseg.gigeresque.client.entity.model

import com.bvanseg.gigeresque.client.entity.animation.EntityAnimations
import com.bvanseg.gigeresque.client.entity.texture.EntityTextures
import com.bvanseg.gigeresque.common.entity.impl.FacehuggerEntity
import net.minecraft.util.Identifier
import software.bernie.geckolib3.model.AnimatedGeoModel

/**
 * @author Boston Vanseghi
 */
class FacehuggerEntityModel : AnimatedGeoModel<FacehuggerEntity>() {
    override fun getModelLocation(entity: FacehuggerEntity): Identifier = EntityModels.FACEHUGGER
    override fun getTextureLocation(entity: FacehuggerEntity): Identifier = EntityTextures.FACEHUGGER
    override fun getAnimationFileLocation(animatable: FacehuggerEntity): Identifier = EntityAnimations.FACEHUGGER
}