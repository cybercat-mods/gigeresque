//package com.bvanseg.gigeresque.common.entity.attribute
//
//import com.bvanseg.gigeresque.common.Gigeresque
//import com.bvanseg.gigeresque.common.util.GigeresqueInitializer
//import com.bvanseg.gigeresque.common.util.initializingBlock
//import net.minecraft.entity.attribute.ClampedEntityAttribute
//import net.minecraft.util.Identifier
//import net.minecraft.util.registry.Registry
//
///**
// * @author Boston Vanseghi
// */
//object AlienEntityAttributes : GigeresqueInitializer {
//
//    const val SABOTAGE_THRESHOLD = 0.85
//    const val SELF_PRESERVE_THRESHOLD = 0.35
//
//    val INTELLIGENCE_ATTRIBUTE =
//        ClampedEntityAttribute("attribute.name.gigeresque.intelligence", 0.5, 0.0, 1.0)
//
//    override fun initialize() = initializingBlock("EntityAttributes") {
//        Registry.register(
//            Registry.ATTRIBUTE,
//            Identifier(Gigeresque.MOD_ID, "attribute.intelligence"),
//            INTELLIGENCE_ATTRIBUTE
//        )
//    }
//}