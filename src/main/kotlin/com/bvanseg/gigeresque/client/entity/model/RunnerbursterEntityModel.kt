package com.bvanseg.gigeresque.client.entity.model

import com.bvanseg.gigeresque.client.entity.animation.EntityAnimations
import com.bvanseg.gigeresque.client.entity.texture.EntityTextures
import com.bvanseg.gigeresque.common.entity.impl.RunnerbursterEntity
import net.minecraft.util.Identifier
import software.bernie.geckolib3.core.event.predicate.AnimationEvent
import software.bernie.geckolib3.model.AnimatedGeoModel
import software.bernie.geckolib3.model.provider.data.EntityModelData

/**
 * @author Boston Vanseghi
 */
class RunnerbursterEntityModel : AnimatedGeoModel<RunnerbursterEntity>() {
    override fun getModelLocation(entity: RunnerbursterEntity): Identifier = EntityModels.RUNNERBURSTER
    override fun getTextureLocation(entity: RunnerbursterEntity): Identifier = EntityTextures.RUNNERBURSTER
    override fun getAnimationFileLocation(animatable: RunnerbursterEntity): Identifier = EntityAnimations.RUNNERBURSTER

    override fun setLivingAnimations(entity: RunnerbursterEntity, uniqueID: Int, customPredicate: AnimationEvent<*>) {
        super.setLivingAnimations(entity, uniqueID, customPredicate)
        val neck = animationProcessor.getBone("neck")
        val extraData = customPredicate.getExtraDataOfType(EntityModelData::class.java).firstOrNull() ?: return
        neck.rotationY = extraData.netHeadYaw * (Math.PI.toFloat() / 340f)
    }
}