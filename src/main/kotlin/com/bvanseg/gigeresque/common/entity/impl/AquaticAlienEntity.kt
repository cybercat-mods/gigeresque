package com.bvanseg.gigeresque.common.entity.impl

import com.bvanseg.gigeresque.common.entity.ai.brain.AquaticAlienBrain
import com.bvanseg.gigeresque.common.entity.ai.brain.sensor.SensorTypes
import com.bvanseg.gigeresque.common.entity.ai.pathing.AmphibiousNavigation
import com.mojang.serialization.Dynamic
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityPose
import net.minecraft.entity.EntityType
import net.minecraft.entity.MovementType
import net.minecraft.entity.ai.brain.Brain
import net.minecraft.entity.ai.brain.MemoryModuleType
import net.minecraft.entity.ai.brain.sensor.Sensor
import net.minecraft.entity.ai.brain.sensor.SensorType
import net.minecraft.entity.ai.control.AquaticLookControl
import net.minecraft.entity.ai.control.AquaticMoveControl
import net.minecraft.entity.ai.control.LookControl
import net.minecraft.entity.ai.control.MoveControl
import net.minecraft.entity.ai.pathing.EntityNavigation
import net.minecraft.entity.ai.pathing.MobNavigation
import net.minecraft.entity.ai.pathing.PathNodeType
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.fluid.Fluid
import net.minecraft.tag.Tag
import net.minecraft.util.math.Vec3d
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
class AquaticAlienEntity(type: EntityType<out AquaticAlienEntity>, world: World) : AdultAlienEntity(type, world) {

    companion object {
        fun createAttributes(): DefaultAttributeContainer.Builder = DefaultAttributeContainer.builder()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 90.0)
            .add(EntityAttributes.GENERIC_ARMOR, 4.0)
            .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 0.0)
            .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.0)
            .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2500000417232513)
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 7.0)
            .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0)

        private val SENSOR_TYPES: List<SensorType<out Sensor<in AquaticAlienEntity>>> =
            listOf(
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorTypes.NEAREST_ALIEN_TARGET,
                SensorTypes.ALIEN_REPELLENT,
            )

        private val MEMORY_MODULE_TYPES: List<MemoryModuleType<*>> =
            listOf(
                MemoryModuleType.ATTACK_TARGET,
                MemoryModuleType.ATTACK_COOLING_DOWN,
                MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
                MemoryModuleType.LOOK_TARGET,
                MemoryModuleType.MOBS,
                MemoryModuleType.NEAREST_REPELLENT,
                MemoryModuleType.PATH,
                MemoryModuleType.VISIBLE_MOBS,
                MemoryModuleType.WALK_TARGET,
            )
    }

    private val animationFactory: AnimationFactory = AnimationFactory(this)

    private lateinit var complexBrain: AquaticAlienBrain

    private val landNavigation = MobNavigation(this, world)
    private val swimNavigation = AmphibiousNavigation(this, world)

    private val landMoveControl = MoveControl(this)
    private val landLookControl = LookControl(this)
    private val swimMoveControl = AquaticMoveControl(this, 85, 10, 0.7f, 1.0f, false)
    private val swimLookControl = AquaticLookControl(this, 10)

    init {
        ignoreCameraFrustum = true

        stepHeight = 1.0f

        navigation = swimNavigation
        moveControl = swimMoveControl
        lookControl = swimLookControl
        setPathfindingPenalty(PathNodeType.WATER, 0.0f)
    }

    override fun createBrainProfile(): Brain.Profile<out AquaticAlienEntity> {
        return Brain.createProfile(MEMORY_MODULE_TYPES, SENSOR_TYPES)
    }

    override fun deserializeBrain(dynamic: Dynamic<*>): Brain<out AquaticAlienEntity> {
        complexBrain = AquaticAlienBrain(this)
        return complexBrain.initialize(createBrainProfile().deserialize(dynamic))
    }

    @Suppress("UNCHECKED_CAST")
    override fun getBrain(): Brain<AquaticAlienEntity> = super.getBrain() as Brain<AquaticAlienEntity>

    override fun mobTick() {
        world.profiler.push("aquaticAlienBrain")
        complexBrain.tick()
        world.profiler.pop()
        complexBrain.tickActivities()
        super.mobTick()
    }

    override fun travel(movementInput: Vec3d?) {
        this.navigation = if (this.isSubmergedInWater || this.isTouchingWater) swimNavigation else landNavigation
        this.moveControl = if (this.submergedInWater || this.isTouchingWater) swimMoveControl else landMoveControl
        this.lookControl = if (this.submergedInWater || this.isTouchingWater) swimLookControl else landLookControl

        if (this.age % 10 == 0) {
            this.calculateDimensions()
        }

        if (canMoveVoluntarily() && this.isTouchingWater) {
            updateVelocity(movementSpeed, movementInput)
            move(MovementType.SELF, velocity)
            velocity = velocity.multiply(0.9)
            if (target == null) {
                velocity = velocity.add(0.0, -0.005, 0.0)
            }
        } else {
            super.travel(movementInput)
        }
    }

    override fun canBreatheInWater(): Boolean = true

    override fun createNavigation(world: World): EntityNavigation = swimNavigation

    override fun isPushedByFluids(): Boolean = false

    override fun swimUpward(fluid: Tag<Fluid>?) = Unit

    override fun getDimensions(pose: EntityPose): EntityDimensions {
        return if (this.submergedInWater) {
            super.getDimensions(pose).scaled(1.0f, 0.5f)
        } else {
            super.getDimensions(pose)
        }
    }

    /*
        ANIMATIONS
     */

    private fun <E : IAnimatable?> predicate(event: AnimationEvent<E>): PlayState {
        val velocityLength = this.velocity.horizontalLength()

        if (this.isSubmergedInWater) {
            return if (this.isAttacking) {
                event.controller.setAnimation(AnimationBuilder().addAnimation("moving_aggro", true))
                PlayState.CONTINUE
            } else {
                event.controller.setAnimation(AnimationBuilder().addAnimation("idle", true))
                PlayState.CONTINUE
            }
        } else {
            return if (velocityLength > 0.0) {
                event.controller.setAnimation(AnimationBuilder().addAnimation("land_moving", true))
                PlayState.CONTINUE
            } else {
                event.controller.setAnimation(AnimationBuilder().addAnimation("land_idle", true))
                PlayState.CONTINUE
            }
        }
    }

    private fun <E : IAnimatable?> hissPredicate(event: AnimationEvent<E>): PlayState {
        if (isHissing) {
            event.controller.setAnimation(AnimationBuilder().addAnimation("hiss_sound", true))
            return PlayState.CONTINUE
        }

        return PlayState.STOP
    }

    override fun registerControllers(data: AnimationData) {
        data.addAnimationController(AnimationController(this, "controller", 10f, this::predicate))
        data.addAnimationController(AnimationController(this, "hissController", 10f, this::hissPredicate))
    }

    override fun getFactory(): AnimationFactory = animationFactory
}