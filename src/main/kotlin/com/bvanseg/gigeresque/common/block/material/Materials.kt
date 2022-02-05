package com.bvanseg.gigeresque.common.block.material

import net.minecraft.block.MapColor

/**
 * @author Boston Vanseghi
 */
object Materials {
    val ACID = MaterialBuilder(MapColor.TERRACOTTA_GREEN).allowsMovement().lightPassesThrough().notSolid().replaceable()
        .destroyedByPiston().build()
    val NEST_RESIN = MaterialBuilder(MapColor.GRAY).burnable().allowsMovement().build()
    val NEST_RESIN_WEB = MaterialBuilder(MapColor.GRAY).burnable().allowsMovement().lightPassesThrough().build()
    val ORGANIC_ALIEN_BLOCK = MaterialBuilder(MapColor.GRAY).build()
    val ROUGH_ALIEN_BLOCK = MaterialBuilder(MapColor.GRAY).build()
    val SINOUS_ALIEN_BLOCK = MaterialBuilder(MapColor.GRAY).build()
}