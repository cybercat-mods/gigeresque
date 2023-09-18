package mods.cybercat.gigeresque.interfacing;

import net.minecraft.world.phys.Vec3;

public interface ILivingEntityTravelHook {
	public boolean onTravel(Vec3 relative, boolean pre);
}
