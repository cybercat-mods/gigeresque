//package com.bvanseg.gigeresque.common.entity.impl
//
//import com.bvanseg.gigeresque.Constants
//import com.bvanseg.gigeresque.common.Gigeresque
//import com.bvanseg.gigeresque.common.data.handler.TrackedDataHandlers
//import com.bvanseg.gigeresque.common.entity.AlienAttackType
//import com.bvanseg.gigeresque.common.entity.attribute.AlienEntityAttributes
//import net.minecraft.entity.EntityType
//import net.minecraft.entity.LivingEntity
//import net.minecraft.entity.attribute.DefaultAttributeContainer
//import net.minecraft.entity.attribute.EntityAttributes
//import net.minecraft.entity.data.DataTracker
//import net.minecraft.entity.data.TrackedData
//import net.minecraft.world.World
//import software.bernie.geckolib3.core.IAnimatable
//import software.bernie.geckolib3.core.PlayState
//import software.bernie.geckolib3.core.builder.AnimationBuilder
//import software.bernie.geckolib3.core.controller.AnimationController
//import software.bernie.geckolib3.core.event.predicate.AnimationEvent
//import software.bernie.geckolib3.core.manager.AnimationData
//import software.bernie.geckolib3.core.manager.AnimationFactory
//
///**
// * @author Boston Vanseghi
// */
//class RunnerAlienEntity(type: EntityType<out RunnerAlienEntity>, world: World) : AdultAlienEntity(type, world) {
//
//    companion object {
//        fun createAttributes(): DefaultAttributeContainer.Builder = LivingEntity.createLivingAttributes()
//            .add(EntityAttributes.GENERIC_MAX_HEALTH, 80.0)
//            .add(EntityAttributes.GENERIC_ARMOR, 4.0)
//            .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 0.0)
//            .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.0)
//            .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0)
//            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23000000417232513)
//            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 7.0 * Constants.ISOLATION_MODE_DAMAGE_BASE)
//            .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0)
//            .add(AlienEntityAttributes.INTELLIGENCE_ATTRIBUTE, 0.5)
//
//        private val CURRENT_ATTACK_TYPE: TrackedData<AlienAttackType> =
//            DataTracker.registerData(RunnerAlienEntity::class.java, TrackedDataHandlers.ALIEN_ATTACK_TYPE)
//    }
//
//    private val animationFactory: AnimationFactory = AnimationFactory(this)
//
//    private var currentAttackType: AlienAttackType
//        get() = dataTracker.get(CURRENT_ATTACK_TYPE)
//        set(value) = dataTracker.set(CURRENT_ATTACK_TYPE, value)
//
//    private var attackProgress = 0
//
//    override fun initDataTracker() {
//        super.initDataTracker()
//        dataTracker.startTracking(CURRENT_ATTACK_TYPE, AlienAttackType.NONE)
//    }
//
//    override fun tick() {
//        super.tick()
//
//        // Attack logic
//
//        if (attackProgress > 0) {
//            attackProgress--
//
//            if (!world.isClient && attackProgress <= 0) {
//                currentAttackType = AlienAttackType.NONE
//            }
//        }
//
//        if (attackProgress == 0 && handSwinging) {
//            attackProgress = 10
//        }
//
//        if (!world.isClient && currentAttackType == AlienAttackType.NONE) {
//            currentAttackType = when (random.nextInt(6)) {
//                0 -> AlienAttackType.CLAW_LEFT
//                1 -> AlienAttackType.CLAW_RIGHT
//                2 -> AlienAttackType.TAIL_LEFT
//                3 -> AlienAttackType.TAIL_RIGHT
//                4 -> AlienAttackType.TAIL_OVER
//                5 -> AlienAttackType.HEAD_BITE
//                else -> AlienAttackType.CLAW_LEFT
//            }
//        }
//    }
//
//    override fun getGrowthMultiplier(): Float = Gigeresque.config.miscellaneous.runnerAlienGrowthMultiplier
//
//    /*
//        ANIMATIONS
//     */
//
//    private fun <E : IAnimatable?> predicate(event: AnimationEvent<E>): PlayState {
//        val velocityLength = this.velocity.horizontalLength()
//
//        return if (velocityLength > 0.0 && !this.isTouchingWater) {
//            if (this.isAttacking) {
//                event.controller.setAnimation(
//                    AnimationBuilder()
//                        .addAnimation("moving_aggro", true)
//                        .addAttackAnimation()
//                )
//                PlayState.CONTINUE
//            } else {
//                event.controller.setAnimation(
//                    AnimationBuilder()
//                        .addAnimation("moving_noaggro", true)
//                        .addAttackAnimation()
//                )
//                PlayState.CONTINUE
//            }
//        } else {
//            event.controller.setAnimation(AnimationBuilder().addAnimation("idle", true))
//            PlayState.CONTINUE
//        }
//    }
//
//    private fun <E : IAnimatable?> attackPredicate(event: AnimationEvent<E>): PlayState {
//        if (currentAttackType != AlienAttackType.NONE && attackProgress > 0) {
//            event.controller.setAnimation(AnimationBuilder().addAttackAnimation())
//            return PlayState.CONTINUE
//        }
//
//        return PlayState.STOP
//    }
//
//    private fun AnimationBuilder.addAttackAnimation(): AnimationBuilder {
////        this.addAnimation(AlienAttackType.animationMappings[currentAttackType], true)
//        return this
//    }
//
//    private fun <E : IAnimatable?> hissPredicate(event: AnimationEvent<E>): PlayState {
//        if (isHissing) {
//            event.controller.setAnimation(AnimationBuilder().addAnimation("hiss_sound", true))
//            return PlayState.CONTINUE
//        }
//
//        return PlayState.STOP
//    }
//
//    override fun registerControllers(data: AnimationData) {
//        data.addAnimationController(AnimationController(this, "controller", 10f, this::predicate))
//        data.addAnimationController(AnimationController(this, "attackController", 0f, this::attackPredicate))
//        data.addAnimationController(AnimationController(this, "hissController", 10f, this::hissPredicate))
//    }
//
//    override fun getFactory(): AnimationFactory = animationFactory
//}