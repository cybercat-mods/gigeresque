package com.bvanseg.gigeresque.common.entity

import com.bvanseg.gigeresque.Constants
import com.bvanseg.gigeresque.common.block.AcidBlock
import com.bvanseg.gigeresque.common.block.Blocks
import com.bvanseg.gigeresque.common.extensions.isNotPuncturing
import net.minecraft.block.AirBlock
import net.minecraft.block.FluidBlock
import net.minecraft.block.TorchBlock
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.brain.MemoryModuleType
import net.minecraft.entity.ai.pathing.PathNodeType
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.state.property.Properties
import net.minecraft.world.World

/**
 * @author Boston Vanseghi
 */
abstract class AlienEntity(type: EntityType<out AlienEntity>, world: World) : HostileEntity(type, world) {

    open val acidDiameter = 3

    init {
        setPathfindingPenalty(PathNodeType.DANGER_FIRE, 16.0f)
        setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, -1.0f)

        navigation?.setCanSwim(true)
    }

    override fun tick() {
        super.tick()

        if (!world.isClient && world.getBlockState(blockPos).block == Blocks.NEST_RESIN && this.age % Constants.TPS == 0) {
            this.heal(0.0833f)
        }
    }

    override fun canImmediatelyDespawn(distanceSquared: Double): Boolean = false
    override fun cannotDespawn(): Boolean = true

    private fun generateAcidPool(xOffset: Int, zOffset: Int) {
        val pos = this.blockPos.add(xOffset, 0, zOffset)
        val posState = world.getBlockState(pos)

        var newState = Blocks.ACID_BLOCK.defaultState

        if (posState.block == net.minecraft.block.Blocks.WATER) {
            newState = newState.with(Properties.WATERLOGGED, true)
        }

        if (posState.block !is AirBlock &&
            posState.block !is FluidBlock &&
            posState.block !is TorchBlock
        ) return

        world.setBlockState(pos, newState)
    }

    override fun onDeath(source: DamageSource) {
        if (source.isNotPuncturing) {
            return super.onDeath(source)
        }

        if (!this.world.isClient) {

            if (acidDiameter == 1) {
                generateAcidPool(0, 0)
            } else {
                val radius = (acidDiameter - 1) / 2

                for (x in -radius..radius) {
                    for (z in -radius..radius) {
                        generateAcidPool(x, z)
                    }
                }
            }
        }
        super.onDeath(source)
    }

    override fun damage(source: DamageSource, amount: Float): Boolean {
        if (!this.world.isClient) {
            source.attacker?.let {
                if (it is LivingEntity) {
                    this.brain.remember(MemoryModuleType.ATTACK_TARGET, it)
                }
            }
        }

        if (source.isNotPuncturing) return super.damage(source, amount)

        if (!this.world.isClient) {
            var acidThickness = if (this.health < (this.maxHealth / 2)) 1 else 0

            if (this.health < (this.maxHealth / 4)) {
                acidThickness += 1
            }

            if (amount >= 5) {
                acidThickness += 1
            }

            if (amount > (this.maxHealth / 10)) {
                acidThickness += 1
            }

            if (acidThickness == 0) return super.damage(source, amount)

            var newState = Blocks.ACID_BLOCK.defaultState.with(AcidBlock.THICKNESS, acidThickness)

            if (this.blockStateAtPos.block == net.minecraft.block.Blocks.WATER) {
                newState = newState.with(Properties.WATERLOGGED, true)
            }

            world.setBlockState(this.blockPos, newState)
        }

        return super.damage(source, amount)
    }
}