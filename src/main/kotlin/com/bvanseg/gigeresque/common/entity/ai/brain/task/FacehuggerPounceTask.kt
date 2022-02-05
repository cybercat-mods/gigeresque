package com.bvanseg.gigeresque.common.entity.ai.brain.task

import com.bvanseg.gigeresque.common.entity.impl.FacehuggerEntity
import com.bvanseg.gigeresque.common.util.clamp
import com.google.common.collect.ImmutableMap
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.brain.MemoryModuleState
import net.minecraft.entity.ai.brain.MemoryModuleType
import net.minecraft.entity.ai.brain.task.Task
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Vec3d
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * @author Boston Vanseghi
 */
class FacehuggerPounceTask : Task<FacehuggerEntity>(
    ImmutableMap.of(
        MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED,
        MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT
    )
) {
    companion object {
        private const val MAX_LEAP_DISTANCE = 8.0
    }

    override fun shouldRun(serverWorld: ServerWorld, facehugger: FacehuggerEntity): Boolean {
        val target = getTarget(facehugger)
        val yDiff = abs(facehugger.blockY - target.blockY)
        return facehugger.isOnGround &&
                facehugger.distanceTo(target) < MAX_LEAP_DISTANCE &&
                yDiff < 3 && facehugger.canSee(target)
    }

    override fun run(serverWorld: ServerWorld, facehugger: FacehuggerEntity, l: Long) {
        val vec3d = facehugger.velocity
        val target = getTarget(facehugger)
        var vec3d2 = Vec3d(target.x - facehugger.x, 0.0, target.z - facehugger.z)
        val length = sqrt(vec3d2.length())

        if (vec3d2.lengthSquared() > 1.0E-7) {
            vec3d2 = vec3d2.normalize().multiply(0.4).add(vec3d.multiply(0.2))
        }

        val maxXVel = clamp(vec3d2.x, -MAX_LEAP_DISTANCE, MAX_LEAP_DISTANCE)
        val maxZVel = clamp(vec3d2.z, -MAX_LEAP_DISTANCE, MAX_LEAP_DISTANCE)

        facehugger.addVelocity(maxXVel * length, target.standingEyeHeight / 2.0, maxZVel * length)
    }

    private fun getTarget(entity: PathAwareEntity): LivingEntity =
        entity.brain.getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get()
}