//package com.bvanseg.gigeresque.common.entity
//
//import com.bvanseg.gigeresque.common.Gigeresque
//import com.bvanseg.gigeresque.common.entity.impl.*
//import net.minecraft.util.Identifier
//import kotlin.reflect.KClass
//
///**
// * @author Boston Vanseghi
// */
//object EntityIdentifiers {
//    val ALIEN = Identifier(Gigeresque.MOD_ID, "alien")
//    val AQUATIC_ALIEN = Identifier(Gigeresque.MOD_ID, "aquatic_alien")
//    val AQUATIC_CHESTBURSTER = Identifier(Gigeresque.MOD_ID, "aquatic_chestburster")
//    val CHESTBURSTER = Identifier(Gigeresque.MOD_ID, "chestburster")
//    val EGG = Identifier(Gigeresque.MOD_ID, "egg")
//    val FACEHUGGER = Identifier(Gigeresque.MOD_ID, "facehugger")
//    val RUNNER_ALIEN = Identifier(Gigeresque.MOD_ID, "runner_alien")
//    val RUNNERBURSTER = Identifier(Gigeresque.MOD_ID, "runnerburster")
//
//    val typeMappings = mapOf<KClass<*>, Identifier>(
//        ClassicAlienEntity::class to ALIEN,
//        AquaticAlienEntity::class to AQUATIC_ALIEN,
//        AquaticChestbursterEntity::class to AQUATIC_CHESTBURSTER,
//        ChestbursterEntity::class to CHESTBURSTER,
//        AlienEggEntity::class to EGG,
//        FacehuggerEntity::class to FACEHUGGER,
//        RunnerAlienEntity::class to RUNNER_ALIEN,
//        RunnerbursterEntity::class to RUNNERBURSTER
//    )
//}