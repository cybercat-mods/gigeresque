//package com.bvanseg.gigeresque.common.entity.impl
//
//import com.bvanseg.gigeresque.Constants
//import com.bvanseg.gigeresque.common.Gigeresque
//import com.bvanseg.gigeresque.common.entity.AlienEntity
//import com.bvanseg.gigeresque.common.entity.Growable
//import com.bvanseg.gigeresque.common.entity.ai.brain.AdultAlienBrain
//import com.bvanseg.gigeresque.common.entity.ai.brain.memory.MemoryModuleTypes
//import com.bvanseg.gigeresque.common.entity.ai.brain.sensor.SensorTypes
//import com.bvanseg.gigeresque.common.extensions.isEggmorphable
//import com.bvanseg.gigeresque.common.sound.Sounds
//import com.mojang.serialization.Dynamic
//import net.minecraft.entity.EntityData
//import net.minecraft.entity.EntityType
//import net.minecraft.entity.LivingEntity
//import net.minecraft.entity.SpawnReason
//import net.minecraft.entity.ai.brain.Brain
//import net.minecraft.entity.ai.brain.MemoryModuleType
//import net.minecraft.entity.ai.brain.sensor.Sensor
//import net.minecraft.entity.ai.brain.sensor.SensorType
//import net.minecraft.entity.ai.pathing.EntityNavigation
//import net.minecraft.entity.ai.pathing.SpiderNavigation
//import net.minecraft.entity.damage.DamageSource
//import net.minecraft.entity.data.DataTracker
//import net.minecraft.entity.data.TrackedData
//import net.minecraft.entity.data.TrackedDataHandlerRegistry
//import net.minecraft.nbt.NbtCompound
//import net.minecraft.sound.SoundEvent
//import net.minecraft.util.math.Vec3d
//import net.minecraft.world.LocalDifficulty
//import net.minecraft.world.ServerWorldAccess
//import net.minecraft.world.World
//import software.bernie.geckolib3.core.IAnimatable
//import kotlin.math.max
//
///**
// * @author Boston Vanseghi
// */
//abstract class AdultAlienEntity(type: EntityType<out AdultAlienEntity>, world: World) : AlienEntity(type, world),
//    IAnimatable, Growable {
//    companion object {
//        private val GROWTH: TrackedData<Float> =
//            DataTracker.registerData(AdultAlienEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
//
//        private val IS_HISSING: TrackedData<Boolean> =
//            DataTracker.registerData(AdultAlienEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
//
//        private val SENSOR_TYPES: List<SensorType<out Sensor<in ClassicAlienEntity>>> =
//            listOf(
//                SensorTypes.NEAREST_ALIEN_WEBBING,
//                SensorType.NEAREST_LIVING_ENTITIES,
//                SensorTypes.NEAREST_ALIEN_TARGET,
//                SensorTypes.ALIEN_REPELLENT,
//                SensorTypes.DESTRUCTIBLE_LIGHT
//            )
//
//        private val MEMORY_MODULE_TYPES: List<MemoryModuleType<*>> =
//            listOf(
//                MemoryModuleType.ATTACK_TARGET,
//                MemoryModuleType.ATTACK_COOLING_DOWN,
//                MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
//                MemoryModuleTypes.EGGMORPH_TARGET,
//                MemoryModuleType.HOME,
//                MemoryModuleType.LOOK_TARGET,
//                MemoryModuleType.MOBS,
//                MemoryModuleTypes.NEAREST_ALIEN_WEBBING,
//                MemoryModuleType.NEAREST_ATTACKABLE,
//                MemoryModuleTypes.NEAREST_LIGHT_SOURCE,
//                MemoryModuleType.NEAREST_REPELLENT,
//                MemoryModuleType.PATH,
//                MemoryModuleType.VISIBLE_MOBS,
//                MemoryModuleType.WALK_TARGET,
//            )
//    }
//
//    init {
//        stepHeight = 1.5f
//    }
//
//    var isHissing: Boolean
//        get() = dataTracker.get(IS_HISSING)
//        set(value) = dataTracker.set(IS_HISSING, value)
//
//    private var hissingCooldown = 0L
//
//    override var growth: Float
//        get() = dataTracker.get(GROWTH)
//        set(value) = dataTracker.set(GROWTH, value)
//
//    private lateinit var complexBrain: AdultAlienBrain
//
//    override fun createBrainProfile(): Brain.Profile<out AdultAlienEntity> {
//        return Brain.createProfile(MEMORY_MODULE_TYPES, SENSOR_TYPES)
//    }
//
//    override fun deserializeBrain(dynamic: Dynamic<*>): Brain<out AdultAlienEntity> {
//        complexBrain = AdultAlienBrain(this)
//        return complexBrain.initialize(createBrainProfile().deserialize(dynamic))
//    }
//
//    @Suppress("UNCHECKED_CAST")
//    override fun getBrain(): Brain<AdultAlienEntity> = super.getBrain() as Brain<AdultAlienEntity>
//
//    override fun mobTick() {
//        world.profiler.push("adultAlienBrain")
//        complexBrain.tick()
//        world.profiler.pop()
//        complexBrain.tickActivities()
//        super.mobTick()
//    }
//
//    override fun initDataTracker() {
//        super.initDataTracker()
//        dataTracker.startTracking(GROWTH, 0.0f)
//        dataTracker.startTracking(IS_HISSING, false)
//    }
//
//    override fun writeCustomDataToNbt(nbt: NbtCompound) {
//        super.writeCustomDataToNbt(nbt)
//        nbt.putFloat("growth", growth)
//        nbt.putBoolean("isHissing", isHissing)
//    }
//
//    override fun readCustomDataFromNbt(nbt: NbtCompound) {
//        super.readCustomDataFromNbt(nbt)
//        if (nbt.contains("growth")) {
//            growth = nbt.getFloat("growth")
//        }
//        if (nbt.contains("isHissing")) {
//            isHissing = nbt.getBoolean("isHissing")
//        }
//    }
//
//    override fun computeFallDamage(fallDistance: Float, damageMultiplier: Float): Int {
//        if (fallDistance <= 9) return 0
//        return super.computeFallDamage(fallDistance, damageMultiplier)
//    }
//
//    override fun getSafeFallDistance(): Int = 9
//
//    override fun initialize(
//        world: ServerWorldAccess,
//        difficulty: LocalDifficulty,
//        spawnReason: SpawnReason,
//        entityData: EntityData?,
//        entityNbt: NbtCompound?
//    ): EntityData? {
//        if (spawnReason != SpawnReason.NATURAL) {
//            growth = maxGrowth
//        }
//        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt)
//    }
//
//    override fun tick() {
//        super.tick()
//
//        if (!world.isClient && this.isAlive) {
//            grow(this, 1 * getGrowthMultiplier())
//        }
//
//        // Hissing Logic
//
//        if (!world.isClient && isHissing) {
//            hissingCooldown = max(hissingCooldown - 1, 0)
//
//            if (hissingCooldown <= 0) {
//                isHissing = false
//            }
//        }
//    }
//
//    override fun damage(source: DamageSource, amount: Float): Boolean {
//        val multiplier = when {
//            source.isFire -> 2.0f
//            source.isProjectile -> 0.5f
//            else -> 1.0f
//        }
//
//        val isolationModeMultiplier = if (Gigeresque.config.features.isolationMode) 0.05f else 1.0f
//
//        return super.damage(source, amount * multiplier * isolationModeMultiplier)
//    }
//
//    override fun isClimbing(): Boolean {
//        val target = this.target
//        val isTargetAbove = target != null && target.blockY > this.blockY
//        return this.horizontalCollision && isTargetAbove
//    }
//
//    override fun createNavigation(world: World): EntityNavigation = SpiderNavigation(this, world)
//
//    fun isCarryingEggmorphableTarget(): Boolean = this.passengerList.isNotEmpty() && this.firstPassenger?.let {
//        it.isEggmorphable()
//    } ?: true
//
//    override fun updatePassengerForDismount(passenger: LivingEntity): Vec3d {
//        if (!this.world.isClient) {
//            complexBrain.stun(Constants.TPS * 3)
//        }
//        return super.updatePassengerForDismount(passenger)
//    }
//
//    /*
//     * GROWTH
//     */
//
//    override val maxGrowth: Float = Constants.TPM.toFloat()
//
//    override fun growInto(): LivingEntity? = null
//
//    /*
//     * SOUNDS
//     */
//
//    override fun getAmbientSound(): SoundEvent = Sounds.ALIEN_AMBIENT
//    override fun getHurtSound(source: DamageSource): SoundEvent = Sounds.ALIEN_HURT
//    override fun getDeathSound(): SoundEvent = Sounds.ALIEN_DEATH
//
//    override fun playAmbientSound() {
//        if (!world.isClient) {
//            isHissing = true
//            hissingCooldown = 80L
//        }
//        super.playAmbientSound()
//    }
//}