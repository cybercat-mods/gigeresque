//package com.bvanseg.gigeresque.common.block.material
//
//import net.minecraft.block.MapColor
//import net.minecraft.block.Material
//import net.minecraft.block.piston.PistonBehavior
//
///**
// * @author Boston Vanseghi
// */
//open class MaterialBuilder(color: MapColor) {
//    private var pistonBehavior: PistonBehavior
//    private var blocksMovement: Boolean
//    private var burnable = false
//    private var liquid = false
//    private var replaceable = false
//    private var solid: Boolean
//    private val color: MapColor
//    private var blocksLight: Boolean
//
//    init {
//        pistonBehavior = PistonBehavior.NORMAL
//        blocksMovement = true
//        solid = true
//        blocksLight = true
//        this.color = color
//    }
//
//    fun liquid(): MaterialBuilder {
//        liquid = true
//        return this
//    }
//
//    fun notSolid(): MaterialBuilder {
//        solid = false
//        return this
//    }
//
//    fun allowsMovement(): MaterialBuilder {
//        blocksMovement = false
//        return this
//    }
//
//    fun lightPassesThrough(): MaterialBuilder {
//        blocksLight = false
//        return this
//    }
//
//    fun burnable(): MaterialBuilder {
//        burnable = true
//        return this
//    }
//
//    fun replaceable(): MaterialBuilder {
//        replaceable = true
//        return this
//    }
//
//    fun destroyedByPiston(): MaterialBuilder {
//        pistonBehavior = PistonBehavior.DESTROY
//        return this
//    }
//
//    fun blocksPistons(): MaterialBuilder {
//        pistonBehavior = PistonBehavior.BLOCK
//        return this
//    }
//
//    fun build(): Material = Material(
//        color,
//        liquid, solid, blocksMovement, blocksLight, burnable, replaceable, pistonBehavior
//    )
//}