package com.bvanseg.gigeresque.common.block

import com.bvanseg.gigeresque.common.entity.AlienEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

/**
 * @author Boston Vanseghi
 */
class CobwebBlock(settings: Settings) : Block(settings) {
    override fun onEntityCollision(state: BlockState, world: World, pos: BlockPos, entity: Entity) {
        if (entity !is AlienEntity) {
            entity.slowMovement(state, Vec3d(0.25, 0.05000000074505806, 0.25))
        }
    }
}
