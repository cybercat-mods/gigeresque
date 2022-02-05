package com.bvanseg.gigeresque.common.entity.impl

import com.bvanseg.gigeresque.Constants
import com.bvanseg.gigeresque.common.entity.AlienEntity
import com.bvanseg.gigeresque.common.entity.Entities
import com.bvanseg.gigeresque.common.entity.ai.brain.AlienEggBrain
import com.bvanseg.gigeresque.common.entity.ai.brain.memory.MemoryModuleTypes
import com.bvanseg.gigeresque.common.entity.ai.brain.sensor.SensorTypes
import com.bvanseg.gigeresque.common.extensions.isPotentialHost
import com.bvanseg.gigeresque.common.extensions.playServerSound
import com.bvanseg.gigeresque.common.sound.Sounds
import com.mojang.serialization.Dynamic
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
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
import net.minecraft.nbt.NbtCompound
import net.minecraft.sound.SoundCategory
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
class AlienEggEntity(type: EntityType<out AlienEggEntity>, world: World) : AlienEntity(type, world), IAnimatable {

    companion object {
        fun createAttributes(): DefaultAttributeContainer.Builder = DefaultAttributeContainer.builder()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
            .add(EntityAttributes.GENERIC_ARMOR, 1.0)
            .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 0.0)
            .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.0)
            .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 0.0)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.0)

        private val IS_HATCHING: TrackedData<Boolean> =
            DataTracker.registerData(AlienEggEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
        private val IS_HATCHED: TrackedData<Boolean> =
            DataTracker.registerData(AlienEggEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
        private val HAS_FACEHUGGER: TrackedData<Boolean> =
            DataTracker.registerData(AlienEggEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)

        private const val MAX_HATCH_PROGRESS = 50L

        private val SENSOR_TYPES: List<SensorType<out Sensor<in AlienEggEntity>>> =
            listOf(
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorTypes.NEAREST_EGGS,
                SensorTypes.NEAREST_FACEHUGGER,
                SensorTypes.NEAREST_HOSTS,
            )

        private val MEMORY_MODULE_TYPES: List<MemoryModuleType<*>> =
            listOf(
                MemoryModuleType.ATTACK_TARGET,
                MemoryModuleType.MOBS,
                MemoryModuleType.VISIBLE_MOBS,
                MemoryModuleTypes.NEAREST_FACEHUGGERS,
                MemoryModuleTypes.NEAREST_EGGS
            )
    }

    override val acidDiameter: Int = 1

    var isHatching: Boolean
        get() = this.dataTracker.get(IS_HATCHING)
        set(value) = this.dataTracker.set(IS_HATCHING, value)

    var isHatched
        get() = this.dataTracker.get(IS_HATCHED)
        set(value) = this.dataTracker.set(IS_HATCHED, value)

    var hasFacehugger
        get() = this.dataTracker.get(HAS_FACEHUGGER)
        set(value) = this.dataTracker.set(HAS_FACEHUGGER, value)

    private var hatchProgress = 0L
    private var ticksOpen = 0L

    private val animationFactory: AnimationFactory = AnimationFactory(this)

    private lateinit var complexBrain: AlienEggBrain

    override fun canImmediatelyDespawn(distanceSquared: Double): Boolean = this.isHatched && !this.hasFacehugger
    override fun cannotDespawn(): Boolean = !this.isHatched && this.hasFacehugger

    override fun createBrainProfile(): Brain.Profile<out AlienEggEntity> {
        return Brain.createProfile(MEMORY_MODULE_TYPES, SENSOR_TYPES)
    }

    override fun deserializeBrain(dynamic: Dynamic<*>): Brain<out AlienEggEntity> {
        complexBrain = AlienEggBrain(this)
        return complexBrain.initialize(createBrainProfile().deserialize(dynamic))
    }

    @Suppress("UNCHECKED_CAST")
    override fun getBrain(): Brain<AlienEggEntity> = super.getBrain() as Brain<AlienEggEntity>

    override fun mobTick() {
        world.profiler.push("alienEggBrain")
        complexBrain.tick()
        world.profiler.pop()
        complexBrain.tickActivities()
        super.mobTick()
    }

    override fun initDataTracker() {
        super.initDataTracker()
        dataTracker.startTracking(IS_HATCHING, false)
        dataTracker.startTracking(IS_HATCHED, false)
        dataTracker.startTracking(HAS_FACEHUGGER, true)
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putBoolean("isHatching", isHatching)
        nbt.putBoolean("isHatched", isHatched)
        nbt.putBoolean("hasFacehugger", hasFacehugger)
        nbt.putLong("hatchProgress", hatchProgress)
        nbt.putLong("ticksOpen", ticksOpen)
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        if(nbt.contains("isHatching")) { isHatching = nbt.getBoolean("isHatching") }
        if(nbt.contains("isHatched")) { isHatched = nbt.getBoolean("isHatched") }
        if(nbt.contains("hasFacehugger")) { hasFacehugger = nbt.getBoolean("hasFacehugger") }
        if(nbt.contains("hatchProgress")) { hatchProgress = nbt.getLong("hatchProgress") }
        if(nbt.contains("ticksOpen")) { ticksOpen = nbt.getLong("ticksOpen") }
    }

    override fun tick() {
        super.tick()

        if (isHatching && hatchProgress < MAX_HATCH_PROGRESS) {
            hatchProgress++
        }

        if (hatchProgress == 15L) {
            world.playServerSound(null, this.blockPos, Sounds.EGG_OPEN, SoundCategory.NEUTRAL, 1.0f)
        }

        if (hatchProgress >= MAX_HATCH_PROGRESS) {
            isHatching = false
            isHatched = true
        }

        if (isHatched && hasFacehugger) {
            ticksOpen++
        }

        if (ticksOpen >= 3L * Constants.TPS && hasFacehugger && !world.isClient) {
            val facehugger = FacehuggerEntity(Entities.FACEHUGGER, world)
            facehugger.refreshPositionAndAngles(blockPos, yaw, pitch)
            facehugger.setVelocity(0.0, 0.7, 0.0)
            world.spawnEntity(facehugger)
            hasFacehugger = false
        }
    }

    /**
     * Prevents entity collisions from moving the egg.
     */
    override fun pushAway(entity: Entity) {
        if (!world.isClient && entity.isPotentialHost()) {
            isHatching = true
        }
    }

    /**
     * Prevents the egg from being pushed.
     */
    override fun isPushable(): Boolean = false

    /**
     * Prevents fluids from moving the egg.
     */
    override fun isPushedByFluids(): Boolean = false

    /**
     * Prevents the egg from moving on its own.
     */
    override fun movesIndependently(): Boolean = false

    /**
     * Prevents the egg moving when hit.
     */
    override fun takeKnockback(strength: Double, x: Double, z: Double) = Unit

    override fun damage(source: DamageSource, amount: Float): Boolean {
        if (source.source != null) {
            isHatching = true
        }
        return super.damage(source, amount)
    }

    /**
     * Prevents the egg from drowning.
     */
    override fun canBreatheInWater(): Boolean = true

    /*
        ANIMATIONS
     */

    private fun <E : IAnimatable> predicate(event: AnimationEvent<E>): PlayState {
        if (isHatched) {
            if (!hasFacehugger) {
                event.controller.setAnimation(AnimationBuilder().addAnimation("open_loop_nobag", true))
                return PlayState.CONTINUE
            }

            event.controller.setAnimation(AnimationBuilder().addAnimation("open_loop", true))
            return PlayState.CONTINUE
        }

        if (isHatching) {
            event.controller.setAnimation(AnimationBuilder().addAnimation("hatching", false).addAnimation("open_loop"))
            return PlayState.CONTINUE
        }

        return PlayState.CONTINUE
    }

    override fun registerControllers(data: AnimationData) {
        data.addAnimationController(AnimationController(this, "controller", 0f, this::predicate))
    }

    override fun getFactory(): AnimationFactory = animationFactory
}