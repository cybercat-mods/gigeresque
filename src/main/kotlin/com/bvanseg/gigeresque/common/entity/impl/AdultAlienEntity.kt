package com.bvanseg.gigeresque.common.entity.impl

import com.bvanseg.gigeresque.Constants
import com.bvanseg.gigeresque.common.entity.AlienEntity
import com.bvanseg.gigeresque.common.entity.Growable
import com.bvanseg.gigeresque.common.extensions.isEggmorphable
import com.bvanseg.gigeresque.common.sound.Sounds
import net.minecraft.entity.EntityData
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.ai.pathing.EntityNavigation
import net.minecraft.entity.ai.pathing.SpiderNavigation
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.sound.SoundEvent
import net.minecraft.world.LocalDifficulty
import net.minecraft.world.ServerWorldAccess
import net.minecraft.world.World
import software.bernie.geckolib3.core.IAnimatable
import kotlin.math.max
import kotlin.math.min

/**
 * @author Boston Vanseghi
 */
abstract class AdultAlienEntity(type: EntityType<out AdultAlienEntity>, world: World): AlienEntity(type, world), IAnimatable, Growable {
    companion object {
        private val GROWTH: TrackedData<Int> =
            DataTracker.registerData(AdultAlienEntity::class.java, TrackedDataHandlerRegistry.INTEGER)

        private val IS_HISSING: TrackedData<Boolean> =
            DataTracker.registerData(AdultAlienEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
    }

    init {
        stepHeight = 1.5f
    }

    var isHissing: Boolean
        get() = dataTracker.get(IS_HISSING)
        set(value) = dataTracker.set(IS_HISSING, value)

    private var hissingCooldown = 0L

    override var growth: Int
        get() = dataTracker.get(GROWTH)
        set(value) = dataTracker.set(GROWTH, value)

    override fun initDataTracker() {
        super.initDataTracker()
        dataTracker.startTracking(GROWTH, 0)
        dataTracker.startTracking(IS_HISSING, false)
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putInt("growth", growth)
        nbt.putBoolean("isHissing", isHissing)
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        if (nbt.contains("growth")) { growth = nbt.getInt("growth") }
        if (nbt.contains("isHissing")) { isHissing = nbt.getBoolean("isHissing") }
    }

    override fun computeFallDamage(fallDistance: Float, damageMultiplier: Float): Int {
        if (fallDistance <= 9) return 0
        return super.computeFallDamage(fallDistance, damageMultiplier)
    }

    override fun getSafeFallDistance(): Int = 9

    override fun initialize(
        world: ServerWorldAccess,
        difficulty: LocalDifficulty,
        spawnReason: SpawnReason,
        entityData: EntityData?,
        entityNbt: NbtCompound?
    ): EntityData? {
        if (spawnReason != SpawnReason.NATURAL) {
            growth = maxGrowth.toInt()
        }
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt)
    }

    override fun tick() {
        super.tick()

        if (!world.isClient && this.isAlive) {
            grow(1)
        }

        // Hissing Logic

        if (!world.isClient && isHissing) {
            hissingCooldown = max(hissingCooldown - 1, 0)

            if (hissingCooldown <= 0) {
                isHissing = false
            }
        }
    }

    override fun damage(source: DamageSource, amount: Float): Boolean {
        val multiplier = when {
            source.isFire -> 2.0f
            source.isProjectile -> 0.5f
            else -> 1.0f
        }
        return super.damage(source, amount * multiplier)
    }

    override fun isClimbing(): Boolean {
        val target = this.target
        val isTargetAbove = target != null && target.blockY > this.blockY
        return this.horizontalCollision && isTargetAbove
    }

    override fun createNavigation(world: World): EntityNavigation = SpiderNavigation(this, world)

    fun isCarryingEggmorphableTarget(): Boolean = this.passengerList.isNotEmpty() && this.firstPassenger?.let {
        it.isEggmorphable()
    } ?: true

    /*
     * GROWTH
     */

    override val maxGrowth : Int= Constants.TPM

    override fun grow(amount: Int) {
        growth = min(growth + amount, maxGrowth)

        if (growth >= maxGrowth) {
            growUp(this)
        }
    }

    override fun growInto(): LivingEntity? = null

    /*
     * SOUNDS
     */

    override fun getAmbientSound(): SoundEvent = Sounds.ALIEN_AMBIENT
    override fun getHurtSound(source: DamageSource): SoundEvent = Sounds.ALIEN_HURT
    override fun getDeathSound(): SoundEvent = Sounds.ALIEN_DEATH

    override fun playAmbientSound() {
        if (!world.isClient) {
            isHissing = true
            hissingCooldown = 80L
        }
        super.playAmbientSound()
    }
}