package com.bvanseg.gigeresque.common.entity.ai.brain.task

import com.bvanseg.gigeresque.common.entity.AlienEntity
import com.bvanseg.gigeresque.common.entity.ai.brain.memory.MemoryModuleTypes
import com.bvanseg.gigeresque.common.extensions.getOrNull
import com.google.common.collect.ImmutableMap
import net.minecraft.entity.ai.brain.MemoryModuleState
import net.minecraft.entity.ai.brain.MemoryModuleType
import net.minecraft.entity.ai.brain.task.Task
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.world.GameRules
import net.minecraft.world.LightType

/**
 * @author Boston Vanseghi
 */
class DestroyLightTask(private val speed: Double) : Task<AlienEntity>(
    ImmutableMap.of(
        MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED,
        MemoryModuleTypes.NEAREST_LIGHT_SOURCE, MemoryModuleState.VALUE_PRESENT
    )
) {

    override fun shouldRun(serverWorld: ServerWorld, alien: AlienEntity): Boolean {
        return alien.world.gameRules.getBoolean(GameRules.DO_MOB_GRIEFING) &&
                alien.world.getLightLevel(LightType.BLOCK, alien.blockPos) != 0
    }

    override fun run(serverWorld: ServerWorld, alien: AlienEntity, l: Long) {
        val lightSourceLocation =
            alien.brain.getOptionalMemory(MemoryModuleTypes.NEAREST_LIGHT_SOURCE).getOrNull() ?: return
        startMovingToTarget(alien, lightSourceLocation)

        if (lightSourceLocation.isWithinDistance(alien.pos, getDesiredDistanceToTarget())) {
            val world = alien.world
            val random = alien.random
            alien.world.removeBlock(lightSourceLocation, false)
            if (!world.isClient) {
                for (i in 0..19) {
                    val e = random.nextGaussian() * 0.02
                    val f = random.nextGaussian() * 0.02
                    val g = random.nextGaussian() * 0.02
                    (world as ServerWorld).spawnParticles(
                        ParticleTypes.POOF,
                        lightSourceLocation.x.toDouble() + 0.5,
                        lightSourceLocation.y.toDouble(),
                        lightSourceLocation.z.toDouble() + 0.5,
                        1,
                        e,
                        f,
                        g,
                        0.15000000596046448
                    )
                }
                alien.brain.forget(MemoryModuleTypes.NEAREST_LIGHT_SOURCE)
            }
        }
    }

    private fun getDesiredDistanceToTarget(): Double = 3.14


    private fun startMovingToTarget(alien: AlienEntity, targetPos: BlockPos) {
        alien.navigation.startMovingTo(
            targetPos.x.toFloat().toDouble() + 0.5,
            (targetPos.y + 1).toDouble(),
            targetPos.z.toFloat()
                .toDouble() + 0.5,
            speed
        )
    }
}