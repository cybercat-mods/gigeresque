package com.bvanseg.gigeresque.common.entity.impl

import com.bvanseg.gigeresque.Constants
import com.bvanseg.gigeresque.common.Gigeresque
import com.bvanseg.gigeresque.common.entity.AlienEntity
import com.bvanseg.gigeresque.common.entity.ai.brain.FacehuggerBrain
import com.bvanseg.gigeresque.common.entity.ai.brain.sensor.SensorTypes
import com.bvanseg.gigeresque.common.extensions.isNotPotentialHost
import com.bvanseg.gigeresque.common.extensions.playServerSound
import com.bvanseg.gigeresque.common.sound.Sounds
import com.bvanseg.gigeresque.interfacing.Host
import com.mojang.serialization.Dynamic
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.TargetPredicate
import net.minecraft.entity.ai.brain.Brain
import net.minecraft.entity.ai.brain.MemoryModuleType
import net.minecraft.entity.ai.brain.sensor.Sensor
import net.minecraft.entity.ai.brain.sensor.SensorType
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluid
import net.minecraft.nbt.NbtCompound
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.tag.Tag
import net.minecraft.world.World
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.core.PlayState
import software.bernie.geckolib3.core.builder.AnimationBuilder
import software.bernie.geckolib3.core.controller.AnimationController
import software.bernie.geckolib3.core.event.predicate.AnimationEvent
import software.bernie.geckolib3.core.manager.AnimationData
import software.bernie.geckolib3.core.manager.AnimationFactory

/**
 * @author Boston Vanseghi
 */
class FacehuggerEntity(type: EntityType<out FacehuggerEntity>, world: World) : AlienEntity(type, world), IAnimatable {
    companion object {
        fun createAttributes(): DefaultAttributeContainer.Builder = DefaultAttributeContainer.builder()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 15.0)
            .add(EntityAttributes.GENERIC_ARMOR, 1.0)
            .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 0.0)
            .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.0)
            .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.33000000417232513)

        private val IS_INFERTILE: TrackedData<Boolean> =
            DataTracker.registerData(FacehuggerEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)

        private val SENSOR_TYPES: List<SensorType<out Sensor<in FacehuggerEntity>>> =
            listOf(
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorTypes.NEAREST_HOST,
                SensorTypes.ALIEN_REPELLENT,
            )

        private val MEMORY_MODULE_TYPES: List<MemoryModuleType<*>> =
            listOf(
                MemoryModuleType.ATTACK_TARGET,
                MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
                MemoryModuleType.LOOK_TARGET,
                MemoryModuleType.MOBS,
                MemoryModuleType.NEAREST_REPELLENT,
                MemoryModuleType.PATH,
                MemoryModuleType.VISIBLE_MOBS,
                MemoryModuleType.WALK_TARGET,
            )
    }

    override fun canImmediatelyDespawn(distanceSquared: Double): Boolean = this.isInfertile
    override fun cannotDespawn(): Boolean = !this.isInfertile

    override val acidDiameter: Int = 1

    private val animationFactory: AnimationFactory = AnimationFactory(this)

    private lateinit var complexBrain: FacehuggerBrain

    private var isInfertile: Boolean
        get() = this.dataTracker.get(IS_INFERTILE)
        set(value) = this.dataTracker.set(IS_INFERTILE, value)

    var ticksAttachedToHost = -1L

    private val targetPredicate =
        TargetPredicate.createNonAttackable().setBaseMaxDistance(6.0).setPredicate { true }

    override fun createBrainProfile(): Brain.Profile<out FacehuggerEntity> {
        return Brain.createProfile(MEMORY_MODULE_TYPES, SENSOR_TYPES)
    }

    override fun deserializeBrain(dynamic: Dynamic<*>): Brain<out FacehuggerEntity> {
        complexBrain = FacehuggerBrain(this)
        return complexBrain.initialize(createBrainProfile().deserialize(dynamic))
    }

    @Suppress("UNCHECKED_CAST")
    override fun getBrain(): Brain<FacehuggerEntity> = super.getBrain() as Brain<FacehuggerEntity>

    override fun mobTick() {
        world.profiler.push("facehuggerBrain")
        complexBrain.tick()
        world.profiler.pop()
        complexBrain.tickActivities()
        super.mobTick()
    }

    override fun initDataTracker() {
        super.initDataTracker()
        dataTracker.startTracking(IS_INFERTILE, false)
    }

    private fun detachFromHost(removesParasite: Boolean) {
        this.stopRiding()
        this.ticksAttachedToHost = -1L

        val vehicle = this.vehicle

        if (vehicle is LivingEntity && removesParasite) {
            (vehicle as Host).removeParasite()
        }
    }

    private fun isAttachedToHost(): Boolean = this.vehicle != null && this.vehicle is LivingEntity

    private fun attachToHost(validHost: LivingEntity) {
        this.startRiding(validHost)
        validHost.movementSpeed = 0.0f
    }

    override fun computeFallDamage(fallDistance: Float, damageMultiplier: Float): Int {
        if (fallDistance <= 12) return 0
        return super.computeFallDamage(fallDistance, damageMultiplier)
    }

    override fun getSafeFallDistance(): Int = 12

    override fun tick() {
        super.tick()

        if (isAttachedToHost()) {
            ticksAttachedToHost += Gigeresque.config.miscellaneous.facehuggerAttachTickMultiplier

            val host = (this.vehicle as Host)

            if (ticksAttachedToHost > Constants.TPS * 3 && host.doesNotHaveParasite()) {
                host.ticksUntilImpregnation = Constants.TPD
                world.playServerSound(null, this.blockPos, Sounds.FACEHUGGER_IMPLANT, SoundCategory.NEUTRAL, 0.5f)
                isInfertile = true
            }
        } else {
            ticksAttachedToHost = -1L
        }

        val vehicle = this.vehicle

        if (vehicle != null && ((vehicle is PlayerEntity && vehicle.isCreative) || ticksAttachedToHost >= Constants.TPM * 5)) {
            detachFromHost(vehicle is PlayerEntity && vehicle.isCreative)
        }

        if (isInfertile) {
            this.clearGoalsAndTasks()
            return
        }

        // We do not want to constantly check collision if we do not have to:
        // - If the entity's vehicle is not null, no need to check.
        // - We should only check every 10 ticks to avoid constantly checking collision.
        if (vehicle == null && !isInfertile) {
            val validHost = findClosestValidHost() ?: return

            if (canStartRiding(validHost)) {
                attachToHost(validHost)
            }
        }
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putBoolean("isInfertile", isInfertile)
        nbt.putLong("ticksAttachedToHost", ticksAttachedToHost)
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        if (nbt.contains("isInfertile")) { isInfertile = nbt.getBoolean("isInfertile") }
        if (nbt.contains("ticksAttachedToHost")) { ticksAttachedToHost = nbt.getLong("ticksAttachedToHost") }
    }

    // This prevents the Facehugger from attacking, since we need to use the melee attack goal to move the Facehugger,
    // but we do not want the Facehugger to deal damage on collision.
    override fun tryAttack(target: Entity): Boolean = true

    private fun findClosestValidHost(): LivingEntity? {
        val viableTargets =
            this.world.getEntitiesByClass(LivingEntity::class.java, this.boundingBox.expand(1.5)) { true }

        val closestEntity =
            this.world.getClosestEntity(viableTargets, targetPredicate, this, this.x, this.eyeY, this.z)

        if (closestEntity.isNotPotentialHost()) return null

        return closestEntity
    }

    override fun damage(source: DamageSource, amount: Float): Boolean {
        if (isAttachedToHost() && ticksAttachedToHost < Constants.TPS * 3 && amount >= 5.0f) {
            detachFromHost(true)
        }

        if ((isAttachedToHost() || isInfertile) && (source == DamageSource.DROWN || source == DamageSource.IN_WALL)) {
            return false
        }

        return super.damage(source, amount)
    }

    override fun takeKnockback(strength: Double, x: Double, z: Double) {
        if (!isInfertile) {
            super.takeKnockback(strength, x, z)
        }
    }

    override fun squaredAttackRange(target: LivingEntity): Double = 0.0

    override fun getAmbientSound(): SoundEvent? = if (isAttachedToHost() || isInfertile) null else Sounds.FACEHUGGER_AMBIENT
    override fun getHurtSound(source: DamageSource): SoundEvent? = if (isAttachedToHost() || isInfertile) null else Sounds.FACEHUGGER_HURT
    override fun getDeathSound(): SoundEvent? = if (isAttachedToHost() || isInfertile) null else Sounds.FACEHUGGER_DEATH

    override fun swimUpward(fluid: Tag<Fluid>) {
        if (!this.isInfertile) {
            super.swimUpward(fluid)
        }
    }

    override fun stopRiding() {
        val vehicle = this.vehicle
        if (vehicle != null &&
            vehicle is LivingEntity &&
            vehicle.isAlive &&
            ticksAttachedToHost < Constants.TPM * 5 &&
            (isSubmergedInWater || isTouchingWater)
        ) {
            return
        }
        super.stopRiding()
    }

    /*
        ANIMATIONS
     */

    private fun <E : IAnimatable?> predicate(event: AnimationEvent<E>): PlayState {
        val velocityLength = this.velocity.horizontalLength()

        if (this.vehicle != null && this.vehicle is LivingEntity) {
            event.controller.setAnimation(AnimationBuilder().addAnimation("hugging_loop", true))
            return PlayState.CONTINUE
        }

        if (isInfertile) {
            event.controller.setAnimation(AnimationBuilder().addAnimation("dead_loop", true))
            return PlayState.CONTINUE
        }

        return if (velocityLength > 0.0) {
            if (this.isAttacking) {
                event.controller.setAnimation(AnimationBuilder().addAnimation("crawling_aggro", true))
                PlayState.CONTINUE
            } else {
                event.controller.setAnimation(AnimationBuilder().addAnimation("crawling_noaggro", true))
                PlayState.CONTINUE
            }
        } else {
            event.controller.setAnimation(AnimationBuilder().addAnimation("idle", true))
            PlayState.CONTINUE
        }
    }

    override fun registerControllers(data: AnimationData) {
        data.addAnimationController(AnimationController(this, "controller", 10f, this::predicate))
    }

    override fun getFactory(): AnimationFactory = animationFactory
}