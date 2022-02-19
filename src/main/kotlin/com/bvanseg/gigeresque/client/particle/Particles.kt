//package com.bvanseg.gigeresque.client.particle
//
//import com.bvanseg.gigeresque.common.Gigeresque
//import com.bvanseg.gigeresque.common.util.GigeresqueInitializer
//import com.bvanseg.gigeresque.common.util.initializingBlock
//import net.fabricmc.api.EnvType
//import net.fabricmc.api.Environment
//import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
//import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
//import net.minecraft.particle.DefaultParticleType
//import net.minecraft.util.Identifier
//import net.minecraft.util.registry.Registry
//import com.bvanseg.gigeresque.client.particle.AcidParticle.Factory as AcidParticleFactory
//import com.bvanseg.gigeresque.client.particle.BloodParticle.Factory as BloodParticleFactory
//
///**
// * @author Boston Vanseghi
// */
//@Environment(EnvType.CLIENT)
//object Particles : GigeresqueInitializer {
//
//    val ACID = FabricParticleTypes.simple()
//    val BLOOD = FabricParticleTypes.simple()
//
//    override fun initialize() = initializingBlock("Particles") {
//        registerParticle("acid", ACID, ::AcidParticleFactory)
//        registerParticle("blood", BLOOD, ::BloodParticleFactory)
//    }
//
//    private fun registerParticle(
//        path: String,
//        type: DefaultParticleType,
//        factory: ParticleFactoryRegistry.PendingParticleFactory<DefaultParticleType>
//    ) {
//        Registry.register(Registry.PARTICLE_TYPE, Identifier(Gigeresque.MOD_ID, path), type)
//        ParticleFactoryRegistry.getInstance().register(type, factory)
//    }
//}