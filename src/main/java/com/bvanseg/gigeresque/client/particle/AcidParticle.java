package com.bvanseg.gigeresque.client.particle;

import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;

public class AcidParticle extends SpriteBillboardParticle {
	private boolean reachedGround;
	private final SpriteProvider spriteProvider;

	public AcidParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i,
			SpriteProvider spriteProvider) {
		super(clientWorld, d, e, f);
		velocityX = g;
		velocityY = h;
		velocityZ = i;
		var red = 160 / 255.0f;
		var green = 158 / 255.0f;
		var blue = 9 / 255.0f;
		var colorRed = MathHelper.nextFloat(random, red - 0.05f, red + 0.05f);
		var colorGreen = MathHelper.nextFloat(random, green - 0.05f, green + 0.05f);
		var colorBlue = MathHelper.nextFloat(random, blue - 0.015f, blue + 0.015f);
		setColor(colorRed, colorGreen, colorBlue);
		scale *= 0.75f;
		maxAge = (int) (20.0 / (((double) random.nextFloat()) * 0.8 + 0.2));
		reachedGround = false;
		collidesWithWorld = false;
		this.spriteProvider = spriteProvider;
		setSpriteForAge(spriteProvider);
	}

	@Override
	public void tick() {
		prevPosX = x;
		prevPosY = y;
		prevPosZ = z;
		if (age++ >= maxAge) {
			markDead();
		} else {
			setSpriteForAge(spriteProvider);
			if (onGround) {
				velocityY = 0.0;
				reachedGround = true;
			}
			if (reachedGround) {
				velocityY += 0.002;
			}
			this.move(velocityX, velocityY, velocityZ);
			if (y == prevPosY) {
				velocityX *= 1.1;
				velocityZ *= 1.1;
			}
		}
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	public float getSize(float tickDelta) {
		return scale * MathHelper.clamp((((float) age) + tickDelta) / ((float) maxAge) * 32.0f, 0.0f, 1.0f);
	}

}
