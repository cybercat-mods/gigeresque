package com.bvanseg.gigeresque.common.entity.ai.brain.memory

import com.bvanseg.gigeresque.common.entity.impl.AlienEggEntity
import com.bvanseg.gigeresque.common.entity.impl.FacehuggerEntity
import com.bvanseg.gigeresque.mixins.MemoryModuleTypeInvoker
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.brain.MemoryModuleType
import net.minecraft.util.math.BlockPos

/**
 * @author Boston Vanseghi
 */
object MemoryModuleTypes {
    val EGGMORPH_TARGET: MemoryModuleType<LivingEntity> = MemoryModuleTypeInvoker.register("eggmorph_target")
    val NEAREST_ALIEN_WEBBING: MemoryModuleType<BlockPos> = MemoryModuleTypeInvoker.register("nearest_alien_webbing")
    val NEAREST_EGGS: MemoryModuleType<List<AlienEggEntity>> = MemoryModuleTypeInvoker.register("nearest_eggs")
    val NEAREST_FACEHUGGERS: MemoryModuleType<List<FacehuggerEntity>> = MemoryModuleTypeInvoker.register("nearest_facehuggers")
    val NEAREST_HOSTS: MemoryModuleType<List<LivingEntity>> = MemoryModuleTypeInvoker.register("nearest_hosts")
    val NEAREST_LIGHT_SOURCE: MemoryModuleType<BlockPos> = MemoryModuleTypeInvoker.register("nearest_light_source")
}