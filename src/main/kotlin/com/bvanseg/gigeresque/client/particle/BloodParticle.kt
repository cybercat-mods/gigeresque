package com.bvanseg.gigeresque.client.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import net.minecraft.util.math.MathHelper

/**
 * @author Boston Vanseghi
 */
@Environment(EnvType.CLIENT)
class BloodParticle(
    clientWorld: ClientWorld,
    d: Double,
    e: Double,
    f: Double,
    g: Double,
    h: Double,
    i: Double,
    spriteProvider: SpriteProvider
) :
    SpriteBillboardParticle(clientWorld, d, e, f) {

    private var reachedGround: Boolean
    private val spriteProvider: SpriteProvider

    init {
        field_28786 = 0.96f
        velocityX = g
        velocityY = h
        velocityZ = i
        val red = 170 / 255.0f
        val green = 0 / 255.0f
        val blue = 0 / 255.0f
        colorRed = MathHelper.nextFloat(random, red - 0.05f, red + 0.05f)
        colorGreen = green
        colorBlue = blue
        scale *= 0.75f
        maxAge = (20.0 / (random.nextFloat().toDouble() * 0.8 + 0.2)).toInt()
        reachedGround = false
        collidesWithWorld = true
        this.spriteProvider = spriteProvider
        setSpriteForAge(spriteProvider)
    }

    override fun tick() {
        prevPosX = x
        prevPosY = y
        prevPosZ = z
        if (age++ >= maxAge) {
            markDead()
        } else {
            setSpriteForAge(spriteProvider)
            if (onGround) {
                velocityY = 0.0
                reachedGround = true
            }
            this.move(velocityX, velocityY, velocityZ)
            if (y == prevPosY) {
                velocityX *= 1.1
                velocityZ *= 1.1
            }
            velocityX *= field_28786.toDouble()
            velocityZ *= field_28786.toDouble()
            if (reachedGround) {
                velocityY *= field_28786.toDouble()
            }
        }
    }

    override fun getType(): ParticleTextureSheet {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE
    }

    override fun getSize(tickDelta: Float): Float {
        return scale * MathHelper.clamp((age.toFloat() + tickDelta) / maxAge.toFloat() * 32.0f, 0.0f, 1.0f)
    }

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteProvider) : ParticleFactory<DefaultParticleType> {
        override fun createParticle(
            defaultParticleType: DefaultParticleType,
            clientWorld: ClientWorld,
            d: Double,
            e: Double,
            f: Double,
            g: Double,
            h: Double,
            i: Double
        ): Particle {
            return BloodParticle(clientWorld, d, e, f, g, h, i, spriteProvider)
        }
    }
}