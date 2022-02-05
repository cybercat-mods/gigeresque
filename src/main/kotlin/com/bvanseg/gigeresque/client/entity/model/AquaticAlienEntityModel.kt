package com.bvanseg.gigeresque.client.entity.model

import com.bvanseg.gigeresque.client.entity.animation.EntityAnimations
import com.bvanseg.gigeresque.client.entity.texture.EntityTextures
import com.bvanseg.gigeresque.common.entity.impl.AquaticAlienEntity
import net.minecraft.util.Identifier
import software.bernie.geckolib3.model.AnimatedGeoModel

/**
 * @author Boston Vanseghi
 */
class AquaticAlienEntityModel : AnimatedGeoModel<AquaticAlienEntity>() {
    override fun getModelLocation(entity: AquaticAlienEntity): Identifier = EntityModels.AQUATIC_ALIEN
    override fun getTextureLocation(entity: AquaticAlienEntity): Identifier = EntityTextures.AQUATIC_ALIEN
    override fun getAnimationFileLocation(animatable: AquaticAlienEntity): Identifier = EntityAnimations.AQUATIC_ALIEN
}