package com.bvanseg.gigeresque.client.entity.model

import com.bvanseg.gigeresque.client.entity.animation.EntityAnimations
import com.bvanseg.gigeresque.client.entity.texture.EntityTextures
import com.bvanseg.gigeresque.common.entity.impl.RunnerAlienEntity
import net.minecraft.util.Identifier
import software.bernie.geckolib3.core.event.predicate.AnimationEvent
import software.bernie.geckolib3.model.AnimatedGeoModel
import software.bernie.geckolib3.model.provider.data.EntityModelData

/**
 * @author Boston Vanseghi
 */
class RunnerAlienEntityModel : AnimatedGeoModel<RunnerAlienEntity>() {
    override fun getModelLocation(entity: RunnerAlienEntity): Identifier = EntityModels.RUNNER_ALIEN
    override fun getTextureLocation(entity: RunnerAlienEntity): Identifier = EntityTextures.RUNNER_ALIEN
    override fun getAnimationFileLocation(animatable: RunnerAlienEntity): Identifier = EntityAnimations.RUNNER_ALIEN

    override fun setLivingAnimations(entity: RunnerAlienEntity, uniqueID: Int, customPredicate: AnimationEvent<*>) {
        super.setLivingAnimations(entity, uniqueID, customPredicate)
        val neck = animationProcessor.getBone("neck")
        val extraData = customPredicate.getExtraDataOfType(EntityModelData::class.java).firstOrNull() ?: return
        neck.rotationY = extraData.netHeadYaw * (Math.PI.toFloat() / 340f)
    }
}