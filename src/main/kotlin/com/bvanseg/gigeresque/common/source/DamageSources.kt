package com.bvanseg.gigeresque.common.source

import net.minecraft.entity.damage.DamageSource

/**
 * @author Boston Vanseghi
 */
object DamageSources {
    val ACID = object : DamageSource("alien.acid") {}
    val CHESTBURSTING = object : DamageSource("chestburst") {}
}