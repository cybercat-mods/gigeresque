package com.bvanseg.gigeresque.client.entity.model

import com.bvanseg.gigeresque.client.entity.animation.EntityAnimations
import com.bvanseg.gigeresque.client.entity.texture.EntityTextures
import com.bvanseg.gigeresque.common.entity.impl.ChestbursterEntity
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
class ChestbursterEntityModel : AnimatedGeoModel<ChestbursterEntity>() {
    override fun getModelLocation(entity: ChestbursterEntity): Identifier = EntityModels.CHESTBURSTER
    override fun getTextureLocation(entity: ChestbursterEntity): Identifier = EntityTextures.CHESTBURSTER
    override fun getAnimationFileLocation(animatable: ChestbursterEntity): Identifier = EntityAnimations.CHESTBURSTER

    override fun setLivingAnimations(entity: ChestbursterEntity, uniqueID: Int, customPredicate: AnimationEvent<*>) {
        super.setLivingAnimations(entity, uniqueID, customPredicate)
        val neck = animationProcessor.getBone("neck")
        val extraData = customPredicate.getExtraDataOfType(EntityModelData::class.java).firstOrNull() ?: return
        neck.rotationY = extraData.netHeadYaw * (Math.PI.toFloat() / 340f)
    }
}