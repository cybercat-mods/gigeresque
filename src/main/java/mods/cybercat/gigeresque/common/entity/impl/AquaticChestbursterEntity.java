package mods.cybercat.gigeresque.common.entity.impl;

import mods.cybercat.gigeresque.common.entity.Growable;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;

public class AquaticChestbursterEntity extends ChestbursterEntity implements IAnimatable, Growable {
	public AquaticChestbursterEntity(EntityType<? extends AquaticChestbursterEntity> type, World world) {
		super(type, world);
	}
}
