//package com.bvanseg.gigeresque.client.entity.model
//
//import com.bvanseg.gigeresque.client.entity.animation.EntityAnimations
//import com.bvanseg.gigeresque.client.entity.texture.EntityTextures
//import com.bvanseg.gigeresque.common.entity.impl.AlienEggEntity
//import net.fabricmc.api.EnvType
//import net.fabricmc.api.Environment
//import net.minecraft.util.Identifier
//import software.bernie.geckolib3.model.AnimatedGeoModel
//
///**
// * @author Boston Vanseghi
// */
//@Environment(EnvType.CLIENT)
//class EggEntityModel : AnimatedGeoModel<AlienEggEntity>() {
//    override fun getModelLocation(entity: AlienEggEntity): Identifier = EntityModels.EGG
//    override fun getTextureLocation(entity: AlienEggEntity): Identifier = EntityTextures.EGG
//    override fun getAnimationFileLocation(animatable: AlienEggEntity): Identifier = EntityAnimations.EGG
//}