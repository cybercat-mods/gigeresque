package com.bvanseg.gigeresque.common.block

import com.bvanseg.gigeresque.client.particle.Particles
import com.bvanseg.gigeresque.common.entity.AlienEntity
import com.bvanseg.gigeresque.common.extensions.isAcidResistant
import com.bvanseg.gigeresque.common.source.DamageSources
import com.bvanseg.gigeresque.common.util.clamp
import net.minecraft.block.*
import net.minecraft.block.Blocks
import net.minecraft.entity.Entity
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.entity.boss.WitherEntity
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.IntProperty
import net.minecraft.state.property.Properties
import net.minecraft.tag.BlockTags
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.GameRules
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import java.util.*
import kotlin.math.max

/**
 * @author Boston Vanseghi
 */
class AcidBlock(settings: Settings) : CustomFallingBlock(settings), Waterloggable {

    companion object {
        private const val MAX_THICKNESS = 4

        val THICKNESS: IntProperty = IntProperty.of("thickness", 0, MAX_THICKNESS)
        private val WATERLOGGED: BooleanProperty = Properties.WATERLOGGED
    }

    init {
        defaultState = (stateManager.defaultState.with(WATERLOGGED, false)).with(THICKNESS, MAX_THICKNESS)
    }

    private fun scheduleTickIfNotScheduled(world: World, pos: BlockPos) {
        if (!world.blockTickScheduler.isScheduled(pos, this)) {
            world.blockTickScheduler.schedule(pos, this, 40)
        }
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(THICKNESS, WATERLOGGED)
    }

    override fun onBlockAdded(state: BlockState, world: World, pos: BlockPos, oldState: BlockState, notify: Boolean) {
        scheduleTickIfNotScheduled(world, pos)
    }

    override fun hasRandomTicks(state: BlockState): Boolean = true
    override fun canMobSpawnInside(): Boolean = false
    override fun getBlastResistance(): Float = 100f
    override fun getRenderType(state: BlockState): BlockRenderType = BlockRenderType.INVISIBLE
    override fun canPathfindThrough(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        type: NavigationType
    ): Boolean = true

    override fun getFluidState(state: BlockState): FluidState? {
        return if (state.get(StairsBlock.WATERLOGGED) as Boolean) Fluids.WATER.getStill(false) else super.getFluidState(
            state
        )
    }

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos?,
        neighborPos: BlockPos?
    ): BlockState {
        if (state.get(WATERLOGGED) as Boolean) {
            world.fluidTickScheduler.schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
    }

    private fun getThickness(state: BlockState): Int = state.get(THICKNESS) as Int
    private fun setThickness(world: ServerWorld, pos: BlockPos, state: BlockState, consume: Int = 1) {
        val newThickness = max(getThickness(state) - consume, 0)

        var newState = state.with(THICKNESS, newThickness)

        if (world.getBlockState(pos).block == Blocks.WATER) {
            newState = newState.with(WATERLOGGED, true)
        }

        world.setBlockState(pos, newState)
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        val currentThickness = getThickness(state)

        if (currentThickness <= 0) {
            world.breakBlock(pos, false)
            return
        }

        if (random.nextInt(8 - currentThickness) == 0) {
            val canGrief = world.gameRules.getBoolean(GameRules.DO_MOB_GRIEFING)

            val blockToEat = pos.down()

            if (currentThickness >= 1) {
                setThickness(world, pos, state, clamp(random.nextInt(2) + 1, 1, currentThickness))

                if (canGrief && !world.getBlockState(blockToEat).block.isAcidResistant) {
                    world.breakBlock(blockToEat, false)
                } else {
                    world.breakBlock(pos, false)
                }

            }
        }

        super.scheduledTick(state, world, pos, random)
        scheduleTickIfNotScheduled(world, pos)
    }

    override fun randomTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random?) {
        scheduleTickIfNotScheduled(world, pos)
    }

    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
        if (getThickness(state) == 0) {
            world.breakBlock(pos, false)
            return
        }

        for (i in 0 until (getThickness(state) * 2) + 1) {
            val yOffset = if (state.get(WATERLOGGED) as Boolean) random.nextDouble() else 0.01
            val d = pos.x.toDouble() + random.nextDouble()
            val e = pos.y.toDouble() + yOffset
            val f = pos.z.toDouble() + random.nextDouble()
            world.addImportantParticle(Particles.ACID, d, e, f, 0.0, 0.0, 0.0)
        }
        if (random.nextInt(5 * ((MAX_THICKNESS + 1) - getThickness(state))) == 0) {
            world.playSound(
                pos.x.toDouble(),
                pos.y.toDouble(),
                pos.z.toDouble(),
                SoundEvents.BLOCK_LAVA_EXTINGUISH,
                SoundCategory.BLOCKS,
                0.2f + random.nextFloat() * 0.2f,
                0.9f + random.nextFloat() * 0.15f,
                false
            )

        }
    }

    override fun canFallThrough(state: BlockState): Boolean {
        val material = state.material
        return (state.isAir || state.isIn(BlockTags.FIRE) || material.isReplaceable) && !material.isLiquid
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape = VoxelShapes.empty()

    private fun dealAcidDamage(state: BlockState, entity: Entity) {
        if (entity !is AlienEntity && entity !is WitherEntity) {
            entity.damage(DamageSources.ACID, getThickness(state).toFloat())
        }
    }

    override fun onEntityCollision(state: BlockState, world: World, pos: BlockPos, entity: Entity) {
        dealAcidDamage(state, entity)
    }

    override fun onSteppedOn(world: World, pos: BlockPos, state: BlockState, entity: Entity) {
        dealAcidDamage(state, entity)
    }
}