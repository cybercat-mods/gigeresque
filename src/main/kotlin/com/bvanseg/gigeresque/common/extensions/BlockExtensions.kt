package com.bvanseg.gigeresque.common.extensions

import com.bvanseg.gigeresque.common.config.ConfigAccessor
import net.minecraft.block.Block
import net.minecraft.util.registry.Registry


private fun isBlockAcidResistant(block: Block): Boolean {
    val id = Registry.BLOCK.getId(block)
    val path = id.path
    val namespace = id.namespace
    return ConfigAccessor.mappedAcidResistantBlocks[namespace]?.contains(path) == true
}

/**
 * @author Boston Vanseghi
 */
val Block.isAcidResistant
    get() = isBlockAcidResistant(this)