package com.bvanseg.gigeresque.common.entity.impl

import com.bvanseg.gigeresque.Constants
import com.bvanseg.gigeresque.common.Gigeresque
import com.bvanseg.gigeresque.common.entity.Entities
import com.bvanseg.gigeresque.common.entity.Growable
import com.bvanseg.gigeresque.common.entity.ai.pathing.AmphibiousNavigation
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.MovementType
import net.minecraft.entity.ai.control.AquaticMoveControl
import net.minecraft.entity.ai.control.LookControl
import net.minecraft.entity.ai.control.MoveControl
import net.minecraft.entity.ai.control.YawAdjustingLookControl
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
class AquaticChestbursterEntity(type: EntityType<out AquaticChestbursterEntity>, world: World) :
    ChestbursterEntity(type, world),
    IAnimatable, Growable {

    companion object {
        fun createAttributes(): DefaultAttributeContainer.Builder = LivingEntity.createLivingAttributes()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 15.0)
            .add(EntityAttributes.GENERIC_ARMOR, 2.0)
            .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 0.0)
            .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.0)
            .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23000000417232513)
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0)
            .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.3)
    }

    private val animationFactory: AnimationFactory = AnimationFactory(this)

    override val acidDiameter: Int = 1

    private val landNavigation = MobNavigation(this, world)
    private val swimNavigation = AmphibiousNavigation(this, world)

    private val landMoveControl = MoveControl(this)
    private val landLookControl = LookControl(this)
    private val swimMoveControl = AquaticMoveControl(this, 85, 10, 0.7f, 1.0f, false)
    private val swimLookControl = YawAdjustingLookControl(this, 10)

    init {
        ignoreCameraFrustum = true

        navigation = swimNavigation
        moveControl = swimMoveControl
        lookControl = swimLookControl
        setPathfindingPenalty(PathNodeType.WATER, 0.0f)
    }

    override fun canBreatheInWater(): Boolean = true

    override fun createNavigation(world: World): EntityNavigation = swimNavigation

    override fun isPushedByFluids(): Boolean = false

    override fun swimUpward(fluid: Tag<Fluid>?) = Unit

    override fun travel(movementInput: Vec3d?) {
        this.navigation = if (this.isSubmergedInWater || this.isTouchingWater) swimNavigation else landNavigation
        this.moveControl = if (this.submergedInWater || this.isTouchingWater) swimMoveControl else landMoveControl
        this.lookControl = if (this.submergedInWater || this.isTouchingWater) swimLookControl else landLookControl

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

    /*
     * GROWTH
     */

    override fun getGrowthMultiplier(): Float = Gigeresque.config.miscellaneous.aquaticChestbursterGrowthMultiplier

    override val maxGrowth: Float = Constants.TPD / 2.0f

    override fun growInto(): LivingEntity {
        val entity = AquaticAlienEntity(Entities.AQUATIC_ALIEN, world)

        if (hasCustomName()) {
            entity.customName = this.customName
        }

        return entity
    }

    /*
     * ANIMATIONS
     */

    private fun <E : IAnimatable?> predicate(event: AnimationEvent<E>): PlayState {
        val velocityLength = this.velocity.horizontalLength()

        if (this.isSubmergedInWater) {
            return if (this.isAttacking) {
                event.controller.setAnimation(AnimationBuilder().addAnimation("swim_fast", true))
                PlayState.CONTINUE
            } else {
                event.controller.setAnimation(AnimationBuilder().addAnimation("swim", true))
                PlayState.CONTINUE
            }
        } else {
            return if (velocityLength > 0.0) {
                return if (this.isAttacking) {
                    event.controller.setAnimation(AnimationBuilder().addAnimation("slither_fast", true))
                    PlayState.CONTINUE
                } else {
                    event.controller.setAnimation(AnimationBuilder().addAnimation("slither", true))
                    PlayState.CONTINUE
                }
            } else {
                event.controller.setAnimation(AnimationBuilder().addAnimation("idle", true))
                PlayState.CONTINUE
            }
        }
    }

    override fun registerControllers(data: AnimationData) {
        data.addAnimationController(AnimationController(this, "controller", 10f, this::predicate))
    }

    override fun getFactory(): AnimationFactory = animationFactory
}