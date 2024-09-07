package mods.azure.bettercrawling.entity.mob;

public interface ILivingEntityRotationHook {
	public float getTargetYaw(double x, double y, double z, float yaw, float pitch, int posRotationIncrements);

	public float getTargetPitch(double x, double y, double z, float yaw, float pitch, int posRotationIncrements);

	public float getTargetHeadYaw(float yaw, int rotationIncrements);
}
