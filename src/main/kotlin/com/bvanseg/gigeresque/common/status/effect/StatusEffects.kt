//package com.bvanseg.gigeresque.common.status.effect
//
//import com.bvanseg.gigeresque.common.Gigeresque
//import com.bvanseg.gigeresque.common.status.effect.impl.TraumaStatusEffect
//import com.bvanseg.gigeresque.common.util.GigeresqueInitializer
//import com.bvanseg.gigeresque.common.util.initializingBlock
//import net.minecraft.entity.attribute.EntityAttributeModifier
//import net.minecraft.entity.attribute.EntityAttributes
//import net.minecraft.entity.effect.StatusEffect
//import net.minecraft.util.Identifier
//import net.minecraft.util.registry.Registry
//
///**
// * @author Boston Vanseghi
// */
//object StatusEffects : GigeresqueInitializer {
//    val TRAUMA: StatusEffect = TraumaStatusEffect().addAttributeModifier(
//        EntityAttributes.GENERIC_MAX_HEALTH,
//        "5e5ac802-7542-4418-b56e-548913950563",
//        -0.5,
//        EntityAttributeModifier.Operation.MULTIPLY_BASE
//    )
//
//    override fun initialize() = initializingBlock("StatusEffects") {
//        Registry.register(Registry.STATUS_EFFECT, Identifier(Gigeresque.MOD_ID, "trauma"), TRAUMA)
//    }
//}