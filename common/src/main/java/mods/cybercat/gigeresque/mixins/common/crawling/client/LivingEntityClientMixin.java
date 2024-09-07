package mods.cybercat.gigeresque.mixins.common.crawling.client;

import mods.azure.bettercrawling.entity.mob.ILivingEntityRotationHook;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityClientMixin implements ILivingEntityRotationHook {
	@ModifyVariable(method = "lerpTo", at = @At(value = "HEAD"), ordinal = 0)
	private float onSetPositionAndRotationDirectYaw(float yaw, double x, double y, double z, float yaw2, float pitch, int posRotationIncrements) {
		return this.getTargetYaw(x, y, z, yaw, pitch, posRotationIncrements);
	}

	@Override
	public float getTargetYaw(double x, double y, double z, float yaw, float pitch, int posRotationIncrements) {
		return yaw;
	}

	@ModifyVariable(method = "lerpTo", at = @At(value = "HEAD"), ordinal = 1)
	private float onSetPositionAndRotationDirectPitch(float pitch, double x, double y, double z, float yaw, float pitch2, int posRotationIncrements) {
		return this.getTargetPitch(x, y, z, yaw, pitch, posRotationIncrements);
	}

	@Override
	public float getTargetPitch(double x, double y, double z, float yaw, float pitch, int posRotationIncrements) {
		return pitch;
	}

	@ModifyVariable(method = "lerpHeadTo", at = @At("HEAD"), ordinal = 0)
	private float onSetHeadRotation(float yaw, float yaw2, int rotationIncrements) {
		return this.getTargetHeadYaw(yaw, rotationIncrements);
	}

	@Override
	public float getTargetHeadYaw(float yaw, int rotationIncrements) {
		return yaw;
	}
}
