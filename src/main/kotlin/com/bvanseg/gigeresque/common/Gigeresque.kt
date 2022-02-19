//package com.bvanseg.gigeresque.common
//
//import com.bvanseg.gigeresque.common.block.Blocks
//import com.bvanseg.gigeresque.common.config.GigeresqueConfig
//import com.bvanseg.gigeresque.common.data.handler.TrackedDataHandlers
//import com.bvanseg.gigeresque.common.entity.Entities
//import com.bvanseg.gigeresque.common.fluid.Fluids
//import com.bvanseg.gigeresque.common.item.Items
//import com.bvanseg.gigeresque.common.sound.Sounds
//import com.bvanseg.gigeresque.common.status.effect.StatusEffects
//import com.bvanseg.gigeresque.common.util.initializingBlock
//import me.shedaniel.autoconfig.AutoConfig
//import me.shedaniel.autoconfig.serializer.GsonConfigSerializer
//import net.fabricmc.api.ModInitializer
//import org.slf4j.Logger
//import org.slf4j.LoggerFactory
//import software.bernie.geckolib3.GeckoLib
//
///**
// * @author Boston Vanseghi
// */
//object Gigeresque : ModInitializer {
//
//    lateinit var config: GigeresqueConfig
//
//    val LOGGER: Logger = LoggerFactory.getLogger(this::class.java)
//
//    const val MOD_ID = "gigeresque"
//
//    override fun onInitialize() = initializingBlock("Gigeresque") {
//        AutoConfig.register(GigeresqueConfig::class.java, ::GsonConfigSerializer)
//        config = AutoConfig.getConfigHolder(GigeresqueConfig::class.java).config
//
//        GeckoLib.initialize()
//
//        Items.initialize()
//        Blocks.initialize()
//        Fluids.initialize()
//        Sounds.initialize()
//        StatusEffects.initialize()
//        TrackedDataHandlers.initialize()
//        Entities.initialize()
//    }
//}