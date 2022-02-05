package com.bvanseg.gigeresque.common.entity.impl

import com.bvanseg.gigeresque.Constants
import com.bvanseg.gigeresque.common.Gigeresque
import com.bvanseg.gigeresque.common.config.ConfigAccessor
import com.bvanseg.gigeresque.common.entity.Entities
import com.bvanseg.gigeresque.common.entity.Growable
import com.bvanseg.gigeresque.common.extensions.getOrNull
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
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
class RunnerbursterEntity(type: EntityType<out RunnerbursterEntity>, world: World) : ChestbursterEntity(type, world),
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

    /*
     * GROWTH
     */

    override fun getGrowthMultiplier(): Float = Gigeresque.config.miscellaneous.runnerbursterGrowthMultiplier

    override val maxGrowth: Float = Constants.TPD / 2.0f

    override fun growInto(): LivingEntity? {
        if (hostId == null) return ClassicAlienEntity(Entities.ALIEN, world)

        val variantId = ConfigAccessor.reversedMorphMappings[hostId] ?: return ClassicAlienEntity(Entities.ALIEN, world)
        val identifier = Identifier(variantId)
        val entityType =
            Registry.ENTITY_TYPE.getOrEmpty(identifier).getOrNull() ?: return ClassicAlienEntity(Entities.ALIEN, world)
        val entity = entityType.create(world)

        if (hasCustomName()) {
            entity?.customName = this.customName
        }

        return entity as LivingEntity?
    }

    /*
     * ANIMATIONS
     */

    private fun <E : IAnimatable?> predicate(event: AnimationEvent<E>): PlayState {
        val velocityLength = this.velocity.horizontalLength()

        return if (velocityLength > 0.0 && !this.isTouchingWater) {
            if (this.isAttacking) {
                event.controller.setAnimation(
                    AnimationBuilder()
                        .addAnimation("moving_aggro", true)
                )
                PlayState.CONTINUE
            } else {
                event.controller.setAnimation(
                    AnimationBuilder()
                        .addAnimation("moving_noaggro", true)
                )
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