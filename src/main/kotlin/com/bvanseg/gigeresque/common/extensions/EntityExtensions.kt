package com.bvanseg.gigeresque.common.extensions

import com.bvanseg.gigeresque.common.config.ConfigAccessor
import com.bvanseg.gigeresque.common.entity.AlienEntity
import com.bvanseg.gigeresque.common.entity.impl.ClassicAlienEntity
import com.bvanseg.gigeresque.common.entity.impl.FacehuggerEntity
import com.bvanseg.gigeresque.interfacing.Eggmorphable
import com.bvanseg.gigeresque.interfacing.Host
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityGroup
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.RangedWeaponItem
import net.minecraft.item.SwordItem
import net.minecraft.util.math.Vec3d

/**
 * @author Boston Vanseghi
 */
fun Entity?.isPotentialHost(): Boolean {
    val playerCondition = if (this is PlayerEntity) {
        !this.isCreative && !this.isSpectator
    } else true

    if (ConfigAccessor.isTargetBlacklisted(FacehuggerEntity::class, this)) return false
    if (ConfigAccessor.isTargetWhitelisted(FacehuggerEntity::class, this)) return true

    val vehicleCondition = if (this?.vehicle != null) {
        this.vehicle !is AlienEntity
    } else true

    return this != null &&
            this.isAlive &&
            this is LivingEntity &&
            this !is AlienEntity &&
            this.passengerList.isEmpty() &&
            (this as Host).doesNotHaveParasite() &&
            (this as Eggmorphable).isNotEggmorphing &&
            this.group != EntityGroup.UNDEAD &&
            vehicleCondition &&
            playerCondition
}

fun Entity?.isEggmorphable(): Boolean {
    val playerCondition = if (this is PlayerEntity) {
        !this.isCreative && !this.isSpectator
    } else true

    if (ConfigAccessor.isTargetBlacklisted(ClassicAlienEntity::class, this)) return false
    if (ConfigAccessor.isTargetWhitelisted(ClassicAlienEntity::class, this)) return true

    val weakCondition = if (this is LivingEntity) {
        (this.health / this.maxHealth < 0.25f) || this.health <= 4f
    } else true

    val threatCondition = if (this is LivingEntity) {
        this.activeItem.item is SwordItem || this.activeItem.item is RangedWeaponItem
    } else true

    return this != null &&
            this.isAlive &&
            this is LivingEntity &&
            this !is AlienEntity &&
            (this as Host).doesNotHaveParasite() &&
            (this as Eggmorphable).isNotEggmorphing &&
            this.group != EntityGroup.UNDEAD &&
            playerCondition &&
            weakCondition &&
            !threatCondition
}

/**
 * @author Boston Vanseghi
 */
fun Entity?.isNotPotentialHost(): Boolean = !this.isPotentialHost()

fun Entity?.isFacehuggerAttached(): Boolean = this != null && this.passengerList.any { it is FacehuggerEntity }

/**
 * Determines if another entity is staring at this entity or not.
 * This function is adapted from EndermanEntity#isPlayerStaring.
 *
 * @author Boston Vanseghi
 */
fun Entity.isBeingStaredAtBy(entity: LivingEntity): Boolean {
    val vec3d = entity.getRotationVec(1.0f).normalize()
    var vec3d2 = Vec3d(this.x - entity.x, this.eyeY - entity.eyeY, this.z - entity.z)
    val d = vec3d2.length()
    vec3d2 = vec3d2.normalize()
    val lookDirection = vec3d.dotProduct(vec3d2)
    val isLookingTowards = lookDirection > 0.7 - 0.025 / d
    return isLookingTowards && entity.canSee(this)
}