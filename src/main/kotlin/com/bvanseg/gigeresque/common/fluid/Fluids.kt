//package com.bvanseg.gigeresque.common.fluid
//
//import com.bvanseg.gigeresque.common.Gigeresque
//import com.bvanseg.gigeresque.common.util.GigeresqueInitializer
//import com.bvanseg.gigeresque.common.util.initializingBlock
//import net.minecraft.fluid.Fluid
//import net.minecraft.util.Identifier
//import net.minecraft.util.registry.Registry
//
///**
// * @author Boston Vanseghi
// */
//object Fluids : GigeresqueInitializer {
//
//    private fun <T : Fluid> registerFluid(path: String, fluid: T): T =
//        Registry.register(Registry.FLUID, Identifier(Gigeresque.MOD_ID, path), fluid)
//
//    val BLACK_FLUID_STILL = BlackFluid.Still()
//    val BLACK_FLUID_FLOWING = BlackFluid.Flowing()
//
//    override fun initialize() = initializingBlock("Fluids") {
//        registerFluid("black_fluid_still", BLACK_FLUID_STILL)
//        registerFluid("black_fluid_flowing", BLACK_FLUID_FLOWING)
//    }
//}