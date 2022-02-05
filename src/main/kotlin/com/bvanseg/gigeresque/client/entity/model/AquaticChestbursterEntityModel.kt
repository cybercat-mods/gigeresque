package com.bvanseg.gigeresque.client.entity.model

import com.bvanseg.gigeresque.client.entity.animation.EntityAnimations
import com.bvanseg.gigeresque.client.entity.texture.EntityTextures
import com.bvanseg.gigeresque.common.entity.impl.AquaticChestbursterEntity
import net.minecraft.util.Identifier
import software.bernie.geckolib3.core.event.predicate.AnimationEvent
import software.bernie.geckolib3.model.AnimatedGeoModel
import software.bernie.geckolib3.model.provider.data.EntityModelData

/**
 * @author Boston Vanseghi
 */
class AquaticChestbursterEntityModel : AnimatedGeoModel<AquaticChestbursterEntity>() {
    override fun getModelLocation(entity: AquaticChestbursterEntity): Identifier = EntityModels.AQUATIC_CHESTBURSTER
    override fun getTextureLocation(entity: AquaticChestbursterEntity): Identifier = EntityTextures.AQUATIC_CHESTBURSTER
    override fun getAnimationFileLocation(animatable: AquaticChestbursterEntity): Identifier = EntityAnimations.AQUATIC_CHESTBURSTER

    override fun setLivingAnimations(entity: AquaticChestbursterEntity, uniqueID: Int, customPredicate: AnimationEvent<*>) {
        super.setLivingAnimations(entity, uniqueID, customPredicate)
        val neck = animationProcessor.getBone("neck")
        val extraData = customPredicate.getExtraDataOfType(EntityModelData::class.java).firstOrNull() ?: return
        neck.rotationY = extraData.netHeadYaw * (Math.PI.toFloat() / 340f)
    }
}