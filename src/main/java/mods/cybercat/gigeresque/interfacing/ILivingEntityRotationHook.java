package mods.cybercat.gigeresque.interfacing;

public interface ILivingEntityRotationHook {
	public float getTargetYaw(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport);
	
	public float getTargetPitch(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport);
	
	public float getTargetHeadYaw(float yaw, int rotationIncrements);
}
