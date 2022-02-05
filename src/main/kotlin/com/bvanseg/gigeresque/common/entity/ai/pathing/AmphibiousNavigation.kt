package com.bvanseg.gigeresque.common.entity.ai.pathing

import net.minecraft.entity.ai.pathing.AmphibiousPathNodeMaker
import net.minecraft.entity.ai.pathing.PathNodeNavigator
import net.minecraft.entity.ai.pathing.SwimNavigation
import net.minecraft.entity.mob.MobEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * @author Boston Vanseghi
 */
class AmphibiousNavigation(entity: MobEntity, world: World) : SwimNavigation(entity, world) {
    override fun isAtValidPosition(): Boolean {
        return true
    }

    override fun createPathNodeNavigator(range: Int): PathNodeNavigator {
        nodeMaker = AmphibiousPathNodeMaker(true)
        nodeMaker.setCanOpenDoors(false)
        nodeMaker.setCanEnterOpenDoors(false)
        return PathNodeNavigator(nodeMaker, range)
    }

    override fun isValidPosition(pos: BlockPos): Boolean {
        return !world.getBlockState(pos.down()).isAir
    }
}