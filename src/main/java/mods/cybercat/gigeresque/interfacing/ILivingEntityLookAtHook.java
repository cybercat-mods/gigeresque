package mods.cybercat.gigeresque.interfacing;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.phys.Vec3;

public interface ILivingEntityLookAtHook {
	public Vec3 onLookAt(EntityAnchorArgument.Anchor anchor, Vec3 vec);
}
