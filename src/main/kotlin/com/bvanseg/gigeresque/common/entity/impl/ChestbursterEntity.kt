//package com.bvanseg.gigeresque.common.entity.impl
//
//import com.bvanseg.gigeresque.Constants
//import com.bvanseg.gigeresque.common.Gigeresque
//import com.bvanseg.gigeresque.common.entity.AlienEntity
//import com.bvanseg.gigeresque.common.entity.Entities
//import com.bvanseg.gigeresque.common.entity.Growable
//import com.bvanseg.gigeresque.common.entity.ai.brain.ChestbursterBrain
//import com.bvanseg.gigeresque.common.entity.ai.brain.sensor.SensorTypes
//import com.mojang.serialization.Dynamic
//import net.minecraft.entity.EntityType
//import net.minecraft.entity.LivingEntity
//import net.minecraft.entity.ai.brain.Brain
//import net.minecraft.entity.ai.brain.MemoryModuleType
//import net.minecraft.entity.ai.brain.sensor.Sensor
//import net.minecraft.entity.ai.brain.sensor.SensorType
//import net.minecraft.entity.attribute.DefaultAttributeContainer
//import net.minecraft.entity.attribute.EntityAttributes
//import net.minecraft.entity.data.DataTracker
//import net.minecraft.entity.data.TrackedData
//import net.minecraft.entity.data.TrackedDataHandlerRegistry
//import net.minecraft.nbt.NbtCompound
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
//open class ChestbursterEntity(type: EntityType<out ChestbursterEntity>, world: World) : AlienEntity(type, world),
//    IAnimatable, Growable {
//
//    companion object {
//        fun createAttributes(): DefaultAttributeContainer.Builder = LivingEntity.createLivingAttributes()
//            .add(EntityAttributes.GENERIC_MAX_HEALTH, 15.0)
//            .add(EntityAttributes.GENERIC_ARMOR, 2.0)
//            .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 0.0)
//            .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.0)
//            .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0)
//            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.43000000417232515)
//            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0)
//            .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.3)
//
//        private val SENSOR_TYPES: List<SensorType<out Sensor<in ChestbursterEntity>>> =
//            listOf(
//                SensorType.NEAREST_LIVING_ENTITIES,
//                SensorTypes.ALIEN_REPELLENT,
//                SensorTypes.NEAREST_FOOD_ITEM,
//                SensorTypes.NEAREST_LARGER_THREAT,
//                SensorTypes.NEAREST_SMALLER_TARGET,
//            )
//
//        private val MEMORY_MODULE_TYPES: List<MemoryModuleType<*>> =
//            listOf(
//                MemoryModuleType.ATTACK_TARGET,
//                MemoryModuleType.ATTACK_COOLING_DOWN,
//                MemoryModuleType.AVOID_TARGET,
//                MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
//                MemoryModuleType.LOOK_TARGET,
//                MemoryModuleType.MOBS,
//                MemoryModuleType.NEAREST_ATTACKABLE,
//                MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
//                MemoryModuleType.NEAREST_REPELLENT,
//                MemoryModuleType.PATH,
//                MemoryModuleType.VISIBLE_MOBS,
//                MemoryModuleType.WALK_TARGET,
//            )
//
//        private val GROWTH: TrackedData<Float> =
//            DataTracker.registerData(ChestbursterEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
//    }
//
//    private val animationFactory: AnimationFactory = AnimationFactory(this)
//
//    override var growth: Float
//        get() = dataTracker.get(GROWTH)
//        set(value) = dataTracker.set(GROWTH, value)
//
//    override val acidDiameter: Int = 1
//
//    private lateinit var complexBrain: ChestbursterBrain
//
//    var hostId: String? = null
//
//    override fun initDataTracker() {
//        super.initDataTracker()
//        dataTracker.startTracking(GROWTH, 0.0f)
//    }
//
//    override fun createBrainProfile(): Brain.Profile<out ChestbursterEntity> {
//        return Brain.createProfile(MEMORY_MODULE_TYPES, SENSOR_TYPES)
//    }
//
//    override fun deserializeBrain(dynamic: Dynamic<*>): Brain<out ChestbursterEntity> {
//        complexBrain = ChestbursterBrain(this)
//        return complexBrain.initialize(createBrainProfile().deserialize(dynamic))
//    }
//
//    @Suppress("UNCHECKED_CAST")
//    override fun getBrain(): Brain<ChestbursterEntity> = super.getBrain() as Brain<ChestbursterEntity>
//
//    override fun tick() {
//        super.tick()
//
//        if (!world.isClient && this.isAlive) {
//            grow(this, 1 * getGrowthMultiplier())
//        }
//    }
//
//    override fun mobTick() {
//        world.profiler.push("chestbursterBrain")
//        complexBrain.tick()
//        world.profiler.pop()
//        complexBrain.tickActivities()
//        super.mobTick()
//    }
//
//    override fun writeCustomDataToNbt(nbt: NbtCompound) {
//        super.writeCustomDataToNbt(nbt)
//        nbt.putFloat("growth", growth)
//        hostId?.let { nbt.putString("hostId", hostId) }
//    }
//
//    override fun readCustomDataFromNbt(nbt: NbtCompound) {
//        super.readCustomDataFromNbt(nbt)
//        if (nbt.contains("growth")) {
//            growth = nbt.getFloat("growth")
//        }
//        if (nbt.contains("hostId")) {
//            hostId = nbt.getString("hostId")
//        }
//    }
//
//    /*
//     * GROWTH
//     */
//
//    override fun getGrowthMultiplier(): Float = Gigeresque.config.miscellaneous.chestbursterGrowthMultiplier
//
//    override val maxGrowth: Float = Constants.TPD / 2.0f
//
//    override fun growInto(): LivingEntity? {
//        val entity = RunnerbursterEntity(Entities.RUNNERBURSTER, world)
//        entity.hostId = this.hostId
//
//        if (hasCustomName()) {
//            entity.customName = this.customName
//        }
//
//        return entity
//    }
//
//    /*
//     * ANIMATIONS
//     */
//
//    private fun <E : IAnimatable?> predicate(event: AnimationEvent<E>): PlayState {
//        val velocityLength = this.velocity.horizontalLength()
//
//        return if (velocityLength > 0.0) {
//            event.controller.setAnimation(AnimationBuilder().addAnimation("slither", true))
//            PlayState.CONTINUE
//        } else {
//            event.controller.setAnimation(AnimationBuilder().addAnimation("idle", true))
//            PlayState.CONTINUE
//        }
//    }
//
//    override fun registerControllers(data: AnimationData) {
//        data.addAnimationController(AnimationController(this, "controller", 10f, this::predicate))
//    }
//
//    override fun getFactory(): AnimationFactory = animationFactory
//}