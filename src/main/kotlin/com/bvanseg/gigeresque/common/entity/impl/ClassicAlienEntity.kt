package com.bvanseg.gigeresque.common.entity.impl

import com.bvanseg.gigeresque.Constants
import com.bvanseg.gigeresque.common.Gigeresque
import com.bvanseg.gigeresque.common.data.handler.TrackedDataHandlers
import com.bvanseg.gigeresque.common.entity.AlienAttackType
import com.bvanseg.gigeresque.common.entity.GenericAlienAttackType
import com.bvanseg.gigeresque.common.entity.attribute.AlienEntityAttributes
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ArmorItem
import net.minecraft.world.World
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.core.PlayState
import software.bernie.geckolib3.core.builder.AnimationBuilder
import software.bernie.geckolib3.core.controller.AnimationController
import software.bernie.geckolib3.core.event.predicate.AnimationEvent
import software.bernie.geckolib3.core.manager.AnimationData
import software.bernie.geckolib3.core.manager.AnimationFactory
import kotlin.math.max

/**
 * @author Boston Vanseghi
 */
class ClassicAlienEntity(type: EntityType<out ClassicAlienEntity>, world: World) : AdultAlienEntity(type, world) {

    companion object {
        fun createAttributes(): DefaultAttributeContainer.Builder = LivingEntity.createLivingAttributes()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 100.0)
            .add(EntityAttributes.GENERIC_ARMOR, 6.0)
            .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 0.0)
            .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.0)
            .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.13000000417232513)
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 7.0 * Constants.ISOLATION_MODE_DAMAGE_BASE)
            .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0)
            .add(AlienEntityAttributes.INTELLIGENCE_ATTRIBUTE, 1.0)

        private val CURRENT_ATTACK_TYPE: TrackedData<AlienAttackType> =
            DataTracker.registerData(ClassicAlienEntity::class.java, TrackedDataHandlers.ALIEN_ATTACK_TYPE)
    }

    private val animationFactory: AnimationFactory = AnimationFactory(this)

    private var currentAttackType: AlienAttackType
        get() = dataTracker.get(CURRENT_ATTACK_TYPE)
        set(value) = dataTracker.set(CURRENT_ATTACK_TYPE, value)

    private var attackProgress = 0

    private var isSearching: Boolean = false
    private var searchingLeft: Boolean = false
    private var searchingProgress = 0L
    private var searchingCooldown = 0L

    override fun initDataTracker() {
        super.initDataTracker()
        dataTracker.startTracking(CURRENT_ATTACK_TYPE, AlienAttackType.NONE)
    }

    override fun tick() {
        super.tick()

        // Attack logic

        if (attackProgress > 0) {
            attackProgress--

            if (!world.isClient && attackProgress <= 0) {
                currentAttackType = AlienAttackType.NONE
            }
        }

        if (attackProgress == 0 && handSwinging) {
            attackProgress = 10
        }

        if (!world.isClient && currentAttackType == AlienAttackType.NONE) {
            currentAttackType = when (random.nextInt(6)) {
                0 -> AlienAttackType.CLAW_LEFT
                1 -> AlienAttackType.CLAW_RIGHT
                2 -> AlienAttackType.TAIL_LEFT
                3 -> AlienAttackType.TAIL_RIGHT
                4 -> AlienAttackType.TAIL_OVER
                5 -> AlienAttackType.HEAD_BITE
                else -> AlienAttackType.CLAW_LEFT
            }
        }

        // Searching Logic

        if (world.isClient && this.velocity.horizontalLength() == 0.0 && !this.isAttacking) {
            if (isSearching) {
                if (searchingProgress > Constants.TPS * 3) {
                    searchingProgress = 0
                    searchingCooldown = Constants.TPS * 15L
                    isSearching = false
                } else {
                    searchingProgress++
                }
            } else {
                searchingCooldown = max(searchingCooldown - 1, 0)

                if (searchingCooldown <= 0) {
                    val next = random.nextInt(10)

                    isSearching = next == 0 || next == 1

                    if (isSearching) {
                        searchingLeft = next == 0
                    }
                }
            }
        }
    }

    override fun getGrowthMultiplier(): Float = Gigeresque.config.miscellaneous.alienGrowthMultiplier

    override fun tryAttack(target: Entity): Boolean {
        var additionalDamage = when (currentAttackType.genericAttackType) {
            GenericAlienAttackType.BITE -> 23.0f
            GenericAlienAttackType.TAIL -> 3.0f
            else -> 0.0f
        }

        if (target is LivingEntity && !world.isClient) {
            currentAttackType.genericAttackType.also { genericAttackType ->
                when (genericAttackType) {
                    GenericAlienAttackType.NONE -> Unit
                    GenericAlienAttackType.BITE -> {
                        val helmet = target.armorItems.firstOrNull {
                            val item = it.item
                            return@firstOrNull (item is ArmorItem && item.slotType == EquipmentSlot.HEAD)
                        }

                        if (helmet != null) {
                            helmet.damage(15, this) {}
                            additionalDamage -= 15
                        }
                    }
                    GenericAlienAttackType.CLAW -> {
                        if (target is PlayerEntity && this.random.nextInt(7) == 0) {
                            target.dropItem(target.inventory.mainHandStack, true, false)
                            target.inventory.removeOne(target.inventory.mainHandStack)
                        }
                    }
                    GenericAlienAttackType.TAIL -> {
                        val armorItems = target.armorItems.toList()
                        armorItems.randomOrNull()?.damage(10, this) {}
                    }
                }
            }
        }

        target.damage(
            DamageSource.mob(this),
            additionalDamage
        )

        return super.tryAttack(target)
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
                        .addAttackAnimation()
                )
                PlayState.CONTINUE
            } else {
                event.controller.setAnimation(
                    AnimationBuilder()
                        .addAnimation("moving_noaggro", true)
                        .addAttackAnimation()
                )
                PlayState.CONTINUE
            }
        } else {
            if (!this.isTouchingWater && isSearching && !this.isAttacking) {
                event.controller.setAnimation(
                    AnimationBuilder().addAnimation(
                        if (searchingLeft) "search_left" else "search_right",
                        false
                    )
                )
                return PlayState.CONTINUE
            }

            event.controller.setAnimation(AnimationBuilder().addAnimation("idle", true))
            PlayState.CONTINUE
        }
    }

    private fun <E : IAnimatable?> attackPredicate(event: AnimationEvent<E>): PlayState {
        if (currentAttackType != AlienAttackType.NONE && attackProgress > 0) {
            event.controller.setAnimation(AnimationBuilder().addAttackAnimation())
            return PlayState.CONTINUE
        }

        return PlayState.STOP
    }

    private fun AnimationBuilder.addAttackAnimation(): AnimationBuilder {
        this.addAnimation(AlienAttackType.animationMappings[currentAttackType], true)
        return this
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
        data.addAnimationController(AnimationController(this, "attackController", 0f, this::attackPredicate))
        data.addAnimationController(AnimationController(this, "hissController", 10f, this::hissPredicate))
    }

    override fun getFactory(): AnimationFactory = animationFactory
}