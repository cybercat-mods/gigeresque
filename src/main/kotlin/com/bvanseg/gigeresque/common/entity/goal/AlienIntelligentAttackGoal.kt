package com.bvanseg.gigeresque.common.entity.goal

import com.bvanseg.gigeresque.common.entity.AlienEntity
import com.bvanseg.gigeresque.common.extensions.isBeingStaredAtBy
import com.bvanseg.gigeresque.common.extensions.isFacehuggerAttached
import com.bvanseg.gigeresque.interfacing.Host
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.ai.pathing.Path
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.predicate.entity.EntityPredicates
import net.minecraft.util.Hand
import java.util.*

/**
 * A more advanced version of the MeleeAttackGoal class. This type of goal is more advanced for the following reasons:
 *
 * 1. Stealth is integrated into the logic. Stealth is the product of the following factors:
 * 1a. The alien is aware of its local light level.
 * 1b. The alien is aware of where the target is looking. If the target is looking away from the alien, the alien will
 * remain stealthy.
 * 1c. The logic keeps track of whether or not the alien had been spotted at any point in time. The alien will drop a
 * stealthy approach if it was spotted.
 *
 * @author Boston Vanseghi
 */
@Deprecated("Will no longer be used in new AI system.")
open class AlienIntelligentAttackGoal(private val mob: AlienEntity, private val initialSpeed: Double = 1.0) : Goal() {

    companion object {
        private const val MAX_COOLDOWN = 20
    }

    private val maxSpeed = initialSpeed + (initialSpeed / 2)
    private val minSpeed = initialSpeed - (initialSpeed / 2)

    private var baseSpeed = minSpeed

    init {
        controls = EnumSet.of(Control.MOVE, Control.LOOK)
    }

    private var path: Path? = null
    private var targetX = 0.0
    private var targetY = 0.0
    private var targetZ = 0.0
    private var cooldown = 0
    private var lastUpdateTime: Long = 0
    private var hasBeenSpotted = false

    override fun canStop(): Boolean = false

    override fun canStart(): Boolean {
        val l = mob.world.time
        return if (l - lastUpdateTime < 20L) {
            false
        } else {
            lastUpdateTime = l
            val livingEntity = mob.target
            if (livingEntity == null || !livingEntity.isAlive || livingEntity.isFacehuggerAttached()) {
                false
            } else {
                this.path = mob.navigation.findPathTo(livingEntity, 0)
                path != null || getSquaredMaxAttackDistance(livingEntity) >= mob.squaredDistanceTo(
                    livingEntity.x,
                    livingEntity.y,
                    livingEntity.z
                )
            }
        }
    }

    override fun shouldContinue(): Boolean {
        val livingEntity = mob.target

        return when {
            livingEntity == null || !livingEntity.isAlive -> false
            livingEntity.isFacehuggerAttached() || (livingEntity as Host).hasParasite() -> false
            else -> livingEntity !is PlayerEntity || !livingEntity.isSpectator() && !livingEntity.isCreative
        }
    }

    override fun start() {
        mob.navigation.startMovingAlong(path, baseSpeed)
        mob.isAttacking = true
        cooldown = 0
    }

    override fun stop() {
        val livingEntity = mob.target
        if (!EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
            mob.target = null
        }
        mob.isAttacking = false
        hasBeenSpotted = false
        baseSpeed = minSpeed
        mob.navigation.stop()
    }

    /**
     * Helper function that determines if the Alien is stealthy or not. Accounts for:
     * - Light Level
     * - Target Vision
     * - Whether or not the alien was already spotted
     * - TODO: Account for distance to target
     *  - TODO: Randomize between either rushing when close enough
     *  - TODO: Alternatively, getting closer and performing a fatal attack.
     * - TODO: If in hive and near resin AND seen, hide against resin to maintain stealth
     * - TODO: Break stealth if nearby alien allies are attacked (if alien is "angry")
     */
    private fun isStealthy(livingEntity: LivingEntity): Boolean {
        val wasSeen = mob.isBeingStaredAtBy(livingEntity)

        if (wasSeen) {
            hasBeenSpotted = true
        }

        val isSkyDarkEnough = mob.world.getLightLevel(mob.blockPos) < 5

        return !hasBeenSpotted && !wasSeen && isSkyDarkEnough && !mob.isTouchingWater
    }

    override fun tick() {
        val livingEntity = mob.target ?: return

        if (isStealthy(livingEntity)) {
            baseSpeed = minSpeed
        } else {
            baseSpeed = maxSpeed
        }

        mob.lookControl.lookAt(livingEntity, 30.0f, 30.0f)
        val d = mob.squaredDistanceTo(livingEntity.x, livingEntity.y, livingEntity.z)
        if ((mob.visibilityCache.canSee(livingEntity)) &&
            (targetX == 0.0 && targetY == 0.0 && targetZ == 0.0 ||
                    livingEntity.squaredDistanceTo(targetX, targetY, targetZ) >= 1.0 || mob.random.nextFloat() < 0.05f)
        ) {
            targetX = livingEntity.x
            targetY = livingEntity.y
            targetZ = livingEntity.z
            mob.navigation.startMovingTo(livingEntity, baseSpeed)
        }
        cooldown = (cooldown - 1).coerceAtLeast(0)
        attack(livingEntity, d)
    }

    private fun attack(target: LivingEntity, squaredDistance: Double) {
        val d = getSquaredMaxAttackDistance(target)
        if (squaredDistance <= d && cooldown <= 0) {
            cooldown = MAX_COOLDOWN // Resets cooldown
            mob.swingHand(Hand.MAIN_HAND)
            mob.tryAttack(target)
            hasBeenSpotted = true
        }
    }

    protected open fun getSquaredMaxAttackDistance(entity: LivingEntity): Double {
        return (mob.width * mob.width + entity.width).toDouble()
    }
}