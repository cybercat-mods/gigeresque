package com.bvanseg.gigeresque.client.entity.model

import com.bvanseg.gigeresque.client.entity.animation.EntityAnimations
import com.bvanseg.gigeresque.client.entity.texture.EntityTextures
import com.bvanseg.gigeresque.common.entity.impl.ClassicAlienEntity
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.util.Identifier
import software.bernie.geckolib3.core.event.predicate.AnimationEvent
import software.bernie.geckolib3.model.AnimatedGeoModel
import software.bernie.geckolib3.model.provider.data.EntityModelData

/**
 * @author Boston Vanseghi
 */
@Environment(EnvType.CLIENT)
class AlienEntityModel : AnimatedGeoModel<ClassicAlienEntity>() {
    override fun getModelLocation(entity: ClassicAlienEntity): Identifier = EntityModels.ALIEN
    override fun getTextureLocation(entity: ClassicAlienEntity): Identifier = EntityTextures.ALIEN
    override fun getAnimationFileLocation(animatable: ClassicAlienEntity): Identifier = EntityAnimations.ALIEN

    override fun setLivingAnimations(entity: ClassicAlienEntity, uniqueID: Int, customPredicate: AnimationEvent<*>) {
        super.setLivingAnimations(entity, uniqueID, customPredicate)
        val neck = animationProcessor.getBone("neck")
        val extraData = customPredicate.getExtraDataOfType(EntityModelData::class.java).firstOrNull() ?: return
        neck.rotationY = extraData.netHeadYaw * (Math.PI.toFloat() / 340f)
    }
}